package com.omer.socialapp.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omer.socialapp.dto.GroupBasicDTO;
import com.omer.socialapp.exceptions.GroupNotFoundException;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IGroupLinksMethods;
import com.omer.socialapp.model.Identifier;
import com.omer.socialapp.model.User;
import com.omer.socialapp.service.IGroupService;
import com.omer.socialapp.service.IUserService;
import com.omer.socialapp.service.ValidationService;

@RestController
public class GroupController 
{
	private final IGroupService groupService;
	private final IUserService userService;
	private final ValidationService validationService;
	
	
	@Autowired
	public GroupController(@Qualifier("groupService")IGroupService groupService, 
			@Qualifier("userService")IUserService userService, ValidationService validationService) {
		this.groupService = groupService;
		this.userService = userService;
		this.validationService = validationService;
	}
	
	@GetMapping("/groups")
	public ResponseEntity<CollectionModel<EntityModel<IGroupLinksMethods>>> getAllGroups(
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "desc", required = false) String desc) {
		var body = groupService.toCollectionModel(groupService.getAllGroups(name, desc)
						.stream()
						.map(GroupBasicDTO::new)
						.collect(Collectors.toList()));
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(body);
	}
	
	@PostMapping("/groups")
	public ResponseEntity<EntityModel<IGroupLinksMethods>> addGroup(Authentication authentication,
			@Valid @RequestBody Group group, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			String errors = validationService.processBindingErrors(bindingResult);
			throw new IllegalArgumentException(errors);
		}
		
		group = groupService.addGroup(group, authentication.getName()); 
		//for the response header.. created (201) response should have a "Location" header with a self link..
		URI selfUri = linkTo(methodOn(GroupController.class).getGroup(group.getId())).toUri();
		return ResponseEntity.created(selfUri).body(groupService.toEntityModel(new GroupBasicDTO(group)));
	}
	
	@GetMapping("/groups/{id}")
	public ResponseEntity<EntityModel<IGroupLinksMethods>> getGroup(@PathVariable long id) {
		var group = groupService.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(groupService.toEntityModel(new GroupBasicDTO(group)));
	}
	
	@DeleteMapping("/groups/{id}")
	public ResponseEntity<?> removePage(@PathVariable long id) {
		groupService.removeGroup(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/users/{uid}/groups")
	public ResponseEntity<CollectionModel<EntityModel<IGroupLinksMethods>>> getUserPages(@PathVariable long uid) {
		var groups = userService.getUserGroups(uid)
					.stream()
					.map(GroupBasicDTO::new)
					.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.body(groupService.toCollectionModel(groups));
	}
	
	@PostMapping("/users/{uid}/groups")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> joinUserToGroup(@PathVariable long uid, @RequestBody Identifier<Long> groupIdentifier) {
		long groupId = groupIdentifier.getIdOrThrow();
		User user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		Group group = groupService.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
		
		user.addGroup(group);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/users/{uid}/groups")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> removeUserFromGroup(@PathVariable long uid, @RequestBody Identifier<Long> groupIdentifier) {
		long groupId = groupIdentifier.getIdOrThrow();
		User user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		Group group = groupService.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
		
		user.removeGroup(group);
		return ResponseEntity.noContent().build();
	}
}

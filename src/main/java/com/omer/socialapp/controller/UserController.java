package com.omer.socialapp.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omer.socialapp.dto.UserBasicDTO;
import com.omer.socialapp.exceptions.GeneralException;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.IUserLinksMethods;
import com.omer.socialapp.model.Identifier;
import com.omer.socialapp.model.User;
import com.omer.socialapp.service.IGroupService;
import com.omer.socialapp.service.IPageService;
import com.omer.socialapp.service.IUserService;


@RestController
public class UserController
{
	private final IUserService userService;
	private final IPageService pageService;
	private final IGroupService groupService;
	
	@Autowired
	public UserController(@Qualifier("userService")IUserService userService, 
			@Qualifier("pageService")IPageService pageService, @Qualifier("groupService")IGroupService groupService) {
		this.userService = userService;
		this.pageService = pageService;
		this.groupService = groupService;
	}
	
	@GetMapping("/users")
	public ResponseEntity<CollectionModel<EntityModel<IUserLinksMethods>>> getAllUsers(
			@RequestParam(name = "name", required = false) String nameLike,
			@RequestParam(name = "age", required = false) Integer age) {
		var users = userService.getAllUsers(nameLike, age)
					.stream()
					.map(user -> new UserBasicDTO(user))
					.collect(Collectors.toList());	
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
				.body(userService.toCollectionModel(users));
	}
	
	@PostMapping("/users")
	public ResponseEntity<EntityModel<IUserLinksMethods>> addUser(@RequestBody User user) {
		user = userService.addUser(user); //get created user with real Id
		//for the response header.. created (201) response should have a "Location" header with a self link..
		URI selfUri = linkTo(methodOn(UserController.class).getSingleUser(user.getId())).toUri();
		return ResponseEntity.created(selfUri).body(userService.toEntityModel(new UserBasicDTO(user)));
	}
	
	@GetMapping("/users/{uid}")
	public ResponseEntity<EntityModel<IUserLinksMethods>> getSingleUser(@PathVariable long uid) {
		var user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));	
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(userService.toEntityModel(new UserBasicDTO(user)));
	}
	
	@DeleteMapping("/users/{uid}")
    public ResponseEntity<?> deleteUser(@PathVariable long uid) {
    	if(!userService.isExists(uid))
    		throw new UserNotFoundException(uid);
    	
    	userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }
	
	@PutMapping("/users/{uid}")
    public ResponseEntity<?> updateOrCreateUser(@PathVariable long uid, @RequestBody User newUser) {
		if(userService.isExists(uid)) {
			//newUser fields are optionals and some of them may be null when deserialize by Jackson!			
			User updatedUser = userService.updateUser(uid, newUser);
            return ResponseEntity.ok(userService.toEntityModel(new UserBasicDTO(updatedUser)));         
		} 
		//else - create a new user and return http 201 response.
		User user = userService.addUser(newUser);
		URI selfUri = linkTo(methodOn(UserController.class).getSingleUser(user.getId())).toUri();
		return ResponseEntity.created(selfUri).body(userService.toEntityModel(new UserBasicDTO(user)));
    }
	
	@GetMapping("/users/{uid}/friends")
	public ResponseEntity<CollectionModel<EntityModel<IUserLinksMethods>>> getFriendsOfUser(@PathVariable long uid) {
		var user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));	
		var friends = userService.toCollectionModel(user.getUserFriends());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(friends);
	}
	
	@PostMapping("/users/{uid}/friends")
	@Transactional
	public ResponseEntity<?> addFriend(@PathVariable long uid, @RequestBody Identifier<Long> friendIdentifier) {
		long friendId = friendIdentifier.getIdOrThrow();
		var user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));	
		var friend = userService.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
	
		if(userService.checkIfFriends(user.getId(), friend.getId())) 
			throw new GeneralException("Users "+user.getId()+" and "+friend.getId()+" are already friends.");
		
		user.addFriend(friend);	
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/users/{uid}/friends")
	@Transactional
	public ResponseEntity<?> deleteFriend(@PathVariable long uid, @RequestBody Identifier<Long> friendIdentifier) {
		long friendId = friendIdentifier.getIdOrThrow();
		var user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));	
		var friend = userService.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
	
		if(!userService.checkIfFriends(user.getId(), friend.getId())) 
			throw new IllegalArgumentException("Users "+uid+" and "+friend.getId()+" are not friends");
		
		user.removeFriend(friend);
		friend.removeFriend(user);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/users/{uid}/detail")
	public ResponseEntity<EntityModel<IUserLinksMethods>> getUserDetail(@PathVariable long uid) {
		User user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.body(userService.toEntityModel(user));
	}
	
	@GetMapping("/pages/{id}/users")
	public ResponseEntity<CollectionModel<EntityModel<IUserLinksMethods>>> getPageUsers(@PathVariable long id) {
		var users = pageService.getUsersOfPage(id)
				.stream()
				.map(UserBasicDTO::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.body(userService.toCollectionModel(users));
	}
	
	@GetMapping("/groups/{id}/users")
	public ResponseEntity<CollectionModel<EntityModel<IUserLinksMethods>>> getGroupUsers(@PathVariable long id) {
		var users = groupService.getUsersOfGroup(id)
				.stream()
				.map(UserBasicDTO::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.body(userService.toCollectionModel(users));
	}
}

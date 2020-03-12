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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.omer.socialapp.dto.PageBasicDTO;
import com.omer.socialapp.exceptions.PageNotFoundException;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.IPageLinksMethods;
import com.omer.socialapp.model.Identifier;
import com.omer.socialapp.model.PlainPage;
import com.omer.socialapp.model.User;
import com.omer.socialapp.service.IPageService;
import com.omer.socialapp.service.IUserService;

@RestController
public class PageController 
{
	private final IPageService pageService;
	private final IUserService userService;

	
	@Autowired
	public PageController(@Qualifier("pageService")IPageService pageService, 
			@Qualifier("userService")IUserService userService) {
		this.pageService = pageService;
		this.userService = userService;
	}
	
	@GetMapping("/pages")
	public ResponseEntity<CollectionModel<EntityModel<IPageLinksMethods>>> getAllPages() {
		var body = pageService.toCollectionModel(pageService.getAllPages()
						.stream()
						.map(PageBasicDTO::new)
						.collect(Collectors.toList()));
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(body);
	}
	
	@PostMapping("/pages/plain")
	public ResponseEntity<EntityModel<IPageLinksMethods>> addPlainPage(@RequestBody PlainPage page) {
		page = pageService.addPlainPage(page);  //get created page with real Id
		//for the response header.. created (201) response should have a "Location" header with a self link..
		URI selfUri = linkTo(methodOn(PageController.class).getPage(page.getId())).toUri();
		return ResponseEntity.created(selfUri).body(pageService.toEntityModel(new PageBasicDTO(page)));
	}
	
	@GetMapping("/pages/{id}")
	public ResponseEntity<EntityModel<IPageLinksMethods>> getPage(@PathVariable long id) {
		var page = pageService.findById(id).orElseThrow(() -> new PageNotFoundException(id));
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(pageService.toEntityModel(new PageBasicDTO(page)));
	}
	
	@DeleteMapping("/pages/{id}")
	public ResponseEntity<?> removePage(@PathVariable long id) {
		pageService.removePage(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/users/{uid}/pages")
	public ResponseEntity<CollectionModel<EntityModel<IPageLinksMethods>>> getUserPages(@PathVariable long uid) {
		var pages = userService.getUserPages(uid)
					.stream()
					.map(PageBasicDTO::new)
					.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.body(pageService.toCollectionModel(pages));
	}
	
	@PostMapping("/users/{uid}/pages")
	@Transactional
	public ResponseEntity<?> joinUserToPage(@PathVariable long uid, @RequestBody Identifier<Long> pageIdentifier) {
		long pageId = pageIdentifier.getIdOrThrow();
		User user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		AbstractPage page = pageService.findById(pageId).orElseThrow(() -> new PageNotFoundException(pageId));
		
		user.likePage(page);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/users/{uid}/pages")
	@Transactional
	public ResponseEntity<?> removeUserFromPage(@PathVariable long uid, @RequestBody Identifier<Long> pageIdentifier) {
		long pageId = pageIdentifier.getIdOrThrow();
		User user = userService.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		AbstractPage page = pageService.findById(pageId).orElseThrow(() -> new PageNotFoundException(pageId));
		
		user.unlikePage(page);
		return ResponseEntity.noContent().build();
	}
}

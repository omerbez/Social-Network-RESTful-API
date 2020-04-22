package com.omer.socialapp.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.omer.socialapp.dto.CommentDTO;
import com.omer.socialapp.exceptions.CommentNotFoundException;
import com.omer.socialapp.model.ICommentLinksMethods;
import com.omer.socialapp.model.RawComment;
import com.omer.socialapp.service.ICommentService;
import com.omer.socialapp.service.ValidationService;

@RestController
public class CommentController
{
	private ICommentService commentService;
	private final ValidationService validationService;
	
	
	@Autowired
	public CommentController(@Qualifier("commentService")ICommentService commentService, 
			ValidationService validationService) {
		this.commentService = commentService;
		this.validationService = validationService;
	}
	
	
	@GetMapping("/comments/{id}")
	public ResponseEntity<EntityModel<ICommentLinksMethods>> getComment(@PathVariable long id) {
		var comment = commentService.getCommentById(id).orElseThrow(() -> new CommentNotFoundException(id));	
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(commentService.toEntityModel(new CommentDTO(comment)));
	}
	
	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<CollectionModel<EntityModel<ICommentLinksMethods>>> getCommentsOfPost(@PathVariable long postId) {
		var comments = commentService.getCommentsOfPost(postId)
				.stream()
				.map(CommentDTO::new)
				.collect(Collectors.toList());	
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(commentService.toCollectionModel(comments));
	}
	
	@GetMapping("/users/{userId}/comments")
	public ResponseEntity<CollectionModel<EntityModel<ICommentLinksMethods>>> getCommentsOfUser(@PathVariable long userId) {
		var comments = commentService.getCommentsOfUser(userId)
				.stream()
				.map(CommentDTO::new)
				.collect(Collectors.toList());	
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(commentService.toCollectionModel(comments));
	}
	
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<EntityModel<ICommentLinksMethods>> addComment(@PathVariable long postId, Authentication auth,
			@Valid @RequestBody RawComment rawComment, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			String errors = validationService.processBindingErrors(bindingResult);
			throw new IllegalArgumentException(errors);
		}	
		
		String username = auth.getName();
		var comment = commentService.addComment(postId, rawComment, username);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(commentService.toEntityModel(new CommentDTO(comment)));
	}
}

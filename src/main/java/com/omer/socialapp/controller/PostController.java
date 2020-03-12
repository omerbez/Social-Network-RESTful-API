package com.omer.socialapp.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.omer.socialapp.dto.PostDTO;
import com.omer.socialapp.model.AbstractPost;
import com.omer.socialapp.model.IPostLinksMethods;
import com.omer.socialapp.model.PostOfGroup;
import com.omer.socialapp.model.PostOfPage;
import com.omer.socialapp.model.RawPost;
import com.omer.socialapp.service.IPostService.EntityPostType;
import com.omer.socialapp.service.PostService;


@RestController
public class PostController
{
	private final PostService postService;
	
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/posts/{postId}")
	public ResponseEntity<EntityModel<IPostLinksMethods>> getPost(@PathVariable long postId) {
		AbstractPost post = postService.getPost(postId);
		long subjectId = postService.getPostSubjectId(postId).orElseThrow(() -> new RuntimeException("Couldn't find post's subject"));
		EntityPostType postType = postService.getPostType(postId);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES))
				.body(postService.toEntityModel(new PostDTO(post, subjectId), postType));
	}
	
	@GetMapping("/pages/{pageId}/posts")
	public ResponseEntity<CollectionModel<EntityModel<IPostLinksMethods>>> getAllPagePosts(@PathVariable long pageId) {
		var posts = postService.getAllPostsOfPage(pageId)
				.stream()
				.map(post -> new PostDTO(post, pageId))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
				.body(postService.toCollectionModel(posts, EntityPostType.POST_OF_PAGE));
	}
	
	@GetMapping("/pages/{pageId}/users/{userId}/posts")
	public ResponseEntity<CollectionModel<EntityModel<IPostLinksMethods>>> 
				getAllUserPagePosts(@PathVariable long pageId, @PathVariable long userId) {
		var posts = postService.getUserPostsOfPage(pageId, userId)
				.stream()
				.map(post -> new PostDTO(post, pageId))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
				.body(postService.toCollectionModel(posts, EntityPostType.POST_OF_PAGE));
	}
	
	@PostMapping("/pages/{pageId}/posts")
	public ResponseEntity<EntityModel<IPostLinksMethods>> addPostOfPage(@PathVariable long pageId, @RequestBody RawPost rawPost) {
		PostOfPage post = postService.addPostOfPage(pageId, rawPost);
		URI selfUri = linkTo(methodOn(PostController.class).getPost(post.getId())).toUri();
		
		return ResponseEntity.created(selfUri)
				.body(postService.toEntityModel(new PostDTO(post, pageId), EntityPostType.POST_OF_PAGE));
	}
	
	@GetMapping("/groups/{groupId}/posts")
	public ResponseEntity<CollectionModel<EntityModel<IPostLinksMethods>>> getAllGroupPosts(@PathVariable long groupId) {
		var posts = postService.getAllPostsOfGroup(groupId)
				.stream()
				.map(post -> new PostDTO(post, groupId))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
				.body(postService.toCollectionModel(posts, EntityPostType.POST_OF_GROUP));
	}
	
	@GetMapping("/groups/{groupId}/users/{userId}/posts")
	public ResponseEntity<CollectionModel<EntityModel<IPostLinksMethods>>> 
					getAllUserGroupPosts(@PathVariable long groupId, @PathVariable long userId) {
		var posts = postService.getUserPostsOfGroup(groupId, userId)
				.stream()
				.map(post -> new PostDTO(post, groupId))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
				.body(postService.toCollectionModel(posts, EntityPostType.POST_OF_GROUP));
	}
	
	@PostMapping("/groups/{groupId}/posts")
	public ResponseEntity<EntityModel<IPostLinksMethods>> addPostOfGroup(@PathVariable long groupId, @RequestBody RawPost rawPost) {
		PostOfGroup post = postService.addPostOfGroup(groupId, rawPost);
		URI selfUri = linkTo(methodOn(PostController.class).getPost(post.getId())).toUri();
		
		return ResponseEntity.created(selfUri)
				.body(postService.toEntityModel(new PostDTO(post, groupId), EntityPostType.POST_OF_GROUP));
	}
}

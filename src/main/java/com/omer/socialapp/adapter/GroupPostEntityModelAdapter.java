package com.omer.socialapp.adapter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.omer.socialapp.controller.PostController;
import com.omer.socialapp.model.IPostLinksMethods;

@Component
public class GroupPostEntityModelAdapter implements SimpleRepresentationModelAssembler<IPostLinksMethods>
{
	@Override
	public void addLinks(EntityModel<IPostLinksMethods> resource) {
		if(resource == null || resource.getContent() == null)
			return;
		
		IPostLinksMethods post = resource.getContent();
		resource.add(linkTo(methodOn(PostController.class).getPost(post.getPostId())).withSelfRel());
		resource.add(linkTo(methodOn(PostController.class).getAllGroupPosts(post.getSubjectId())).withRel("allGroupPosts"));
		resource.add(linkTo(methodOn(PostController.class)
				.getAllUserGroupPosts(post.getSubjectId(), post.getUserId()))
				.withRel("UserGroupPosts"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<IPostLinksMethods>> resources) {
		// Post must be related to some subject..
	}
}
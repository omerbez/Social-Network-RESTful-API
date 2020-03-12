package com.omer.socialapp.adapter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.omer.socialapp.controller.CommentController;
import com.omer.socialapp.model.ICommentLinksMethods;

@Component
public class CommentEntityModelAdapter implements SimpleRepresentationModelAssembler<ICommentLinksMethods>
{
	@Override
	public void addLinks(EntityModel<ICommentLinksMethods> resource) {
		if(resource == null || resource.getContent() == null)
			return;
		
		ICommentLinksMethods comment = resource.getContent();
		resource.add(linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel());
		resource.add(linkTo(methodOn(CommentController.class).getCommentsOfPost(comment.getRelatedPostId())).withRel("postComments"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<ICommentLinksMethods>> resources) {
		//No links..
	}
}
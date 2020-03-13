package com.omer.socialapp.adapter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.omer.socialapp.controller.UserController;
import com.omer.socialapp.model.IUserLinksMethods;


@Component
public class UserEntityModelAdapter implements SimpleRepresentationModelAssembler<IUserLinksMethods>
{
	@Override
	public void addLinks(EntityModel<IUserLinksMethods> resource) {
		if(resource == null || resource.getContent() == null)
			return;
		
		resource.add(linkTo(methodOn(UserController.class).getSingleUser(resource.getContent().getId())).withSelfRel());
		resource.add(linkTo(methodOn(UserController.class).getAllUsers(null, null)).withRel("users"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<IUserLinksMethods>> resources) {
		resources.add(linkTo(methodOn(UserController.class).getAllUsers(null, null)).withRel("users"));	
	}
}
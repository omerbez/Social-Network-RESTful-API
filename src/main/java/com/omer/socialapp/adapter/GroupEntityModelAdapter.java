package com.omer.socialapp.adapter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.omer.socialapp.controller.GroupController;
import com.omer.socialapp.model.IGroupLinksMethods;

@Component
public class GroupEntityModelAdapter implements SimpleRepresentationModelAssembler<IGroupLinksMethods>
{
	@Override
	public void addLinks(EntityModel<IGroupLinksMethods> resource) {
		if(resource == null || resource.getContent() == null)
			return;
		
		resource.add(linkTo(methodOn(GroupController.class).getGroup(resource.getContent().getId())).withSelfRel());
		resource.add(linkTo(methodOn(GroupController.class).getAllGroups()).withRel("groups"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<IGroupLinksMethods>> resources) {
		resources.add(linkTo(methodOn(GroupController.class).getAllGroups()).withRel("groups"));	
	}
}
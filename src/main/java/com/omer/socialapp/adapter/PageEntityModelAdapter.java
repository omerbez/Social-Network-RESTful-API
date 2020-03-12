package com.omer.socialapp.adapter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.omer.socialapp.controller.PageController;
import com.omer.socialapp.model.IPageLinksMethods;

@Component
public class PageEntityModelAdapter implements SimpleRepresentationModelAssembler<IPageLinksMethods>
{
	@Override
	public void addLinks(EntityModel<IPageLinksMethods> resource) {
		if(resource == null || resource.getContent() == null)
			return;
		
		resource.add(linkTo(methodOn(PageController.class).getPage(resource.getContent().getId())).withSelfRel());
		resource.add(linkTo(methodOn(PageController.class).getAllPages()).withRel("pages"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<IPageLinksMethods>> resources) {
		resources.add(linkTo(methodOn(PageController.class).getAllPages()).withRel("pages"));	
	}
}
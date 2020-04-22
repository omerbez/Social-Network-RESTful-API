package com.omer.socialapp.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.IPageLinksMethods;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

//change the default name in CollectionModel and EntityModel (the response JSON key name)
//the default is classnameList..
@Relation(collectionRelation = "pagesList", itemRelation = "page") 
@Value
@JsonPropertyOrder(value = {"id", "pageName", "description"})
public class PageBasicDTO implements IPageLinksMethods 
{
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private AbstractPage page;
	
	public PageBasicDTO(AbstractPage page) {
		this.page = page;
	}
	
	@Override
	public Long getId() {
		return page.getId();
	}
	
	public String getPageName() {
		return page.getName();
	}
	
	public String getDescription() {
		return page.getDescription();
	}
	
	public String getOwnerUsername() {
		return page.getOwnerUsername();
	}
}

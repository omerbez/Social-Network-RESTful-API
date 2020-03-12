package com.omer.socialapp.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IGroupLinksMethods;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

//change the default name in CollectionModel and EntityModel (the response JSON key name)
//the default is classnameList..
@Relation(collectionRelation = "groupsList", itemRelation = "group") 
@Value
@JsonPropertyOrder(value = {"id", "groupName", "description"})
public class GroupBasicDTO implements IGroupLinksMethods 
{
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private Group group;
	
	public GroupBasicDTO(Group group) {
		this.group = group;
	}
	
	@Override
	public Long getId() {
		return group.getId();
	}
	
	public String getGroupName() {
		return group.getGroupName();
	}
	
	public String getDescription() {
		return group.getDescription();
	}
}

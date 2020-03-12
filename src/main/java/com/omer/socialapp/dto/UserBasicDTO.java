package com.omer.socialapp.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.omer.socialapp.model.IUserLinksMethods;
import com.omer.socialapp.model.User;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;


//change the default name in CollectionModel and EntityModel (the response JSON key name)
//the default is classnameList..
@Relation(collectionRelation = "usersList", itemRelation = "user") 
@Value
@JsonPropertyOrder(value = {"id", "displayName", "email", "age"})
public class UserBasicDTO implements IUserLinksMethods 
{
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private User user;
	
	public UserBasicDTO(User user) {
		this.user = user;
	}
	
	@Override
	public Long getId() {
		return user.getId();
	}
	
	public String getDisplayName() {
		return user.getDisplayName();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public int getAge() {
		return user.getAge();
	}
}

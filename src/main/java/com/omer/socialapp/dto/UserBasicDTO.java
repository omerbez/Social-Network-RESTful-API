package com.omer.socialapp.dto;

import java.time.LocalDate;

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
@JsonPropertyOrder(value = {"id", "username", "displayName", "email", "age"})
public class UserBasicDTO implements IUserLinksMethods
{
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private User user;
	
	public UserBasicDTO(User user) {
		this.user = user;
	}
	
	public Long getId() {
		return user.getId();
	}
	
	public String getUsername() {
		return user.getUsername();
	}
	
	public String getDisplayName() {
		return user.getDisplayName();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public LocalDate getDateOfBirth() {
		return user.getDateOfBirth();
	}
}

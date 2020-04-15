package com.omer.socialapp.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Identifier<ID> {
	
	@JsonProperty("id")
	private ID id;
	
	@JsonIgnore
	public ID getIdOrThrow() {
		Assert.notNull(id, "Null identifier");
		return id;
	}
}

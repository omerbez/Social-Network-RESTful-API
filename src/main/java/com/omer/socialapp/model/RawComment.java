package com.omer.socialapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawComment
{
	@JsonProperty("userId")
	private Long commentedUserId;
	
	private String text;
}

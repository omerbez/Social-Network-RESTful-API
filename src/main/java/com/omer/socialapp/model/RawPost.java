package com.omer.socialapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawPost
{
	@JsonProperty("userId")
	private Long postedUserId;
	
	private String text;
}

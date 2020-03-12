package com.omer.socialapp.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage
{
	@JsonProperty("time")
	private LocalDateTime time;
	
	@JsonProperty("errorMsg")
	private String message;
	
	public ErrorMessage(String msg) {
		time = LocalDateTime.now();
		message = msg;
	}
}

package com.omer.socialapp.exceptions;

public class PostNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PostNotFoundException(long id) {
		super("Couldn't find post with id "+id);
	}
}

package com.omer.socialapp.exceptions;

public class UserNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(long id) {
		super("Couldn't find user with id "+id);
	}
	
	public UserNotFoundException(String name) {
		super("Couldn't find user with name "+name);
	}

}

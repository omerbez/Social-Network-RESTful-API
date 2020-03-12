package com.omer.socialapp.exceptions;

public class GroupNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public GroupNotFoundException(long id) {
		super("Couldn't find group with id "+id);
	}
	
	public GroupNotFoundException(String name) {
		super("Couldn't find group name "+name);
	}

}

package com.omer.socialapp.exceptions;

public class PageNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public PageNotFoundException(long id) {
		super("Couldn't find page with id "+id);
	}
	
	public PageNotFoundException(String name) {
		super("Couldn't find page name "+name);
	}

}

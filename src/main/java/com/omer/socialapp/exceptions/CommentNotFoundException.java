package com.omer.socialapp.exceptions;

public class CommentNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public CommentNotFoundException(long id) {
		super("Couldn't find comment with id "+id);
	}
}

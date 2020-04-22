package com.omer.socialapp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AnyAuthenticationException extends AuthenticationException{

	private static final long serialVersionUID = 1L;

	public AnyAuthenticationException() {
		super("Authentication failure occured");
	}
	
	public AnyAuthenticationException(String msg) {
		super(msg);
	}
}

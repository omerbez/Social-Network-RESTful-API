package com.omer.socialapp.model;

import com.omer.socialapp.validation.Password;
import com.omer.socialapp.validation.Username;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestParams {
	@Username
	private String username;
	
	@Password
    private String password;
}
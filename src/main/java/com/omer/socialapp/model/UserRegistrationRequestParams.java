package com.omer.socialapp.model;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.omer.socialapp.validation.EmailsMatch;
import com.omer.socialapp.validation.Password;
import com.omer.socialapp.validation.PasswordsMatch;
import com.omer.socialapp.validation.Username;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PasswordsMatch
@EmailsMatch
public class UserRegistrationRequestParams 
{
	@NotNull(message = "Display name is mandatory")
	@Size(min = 3, max = 15, message = "Display name length must be 3 to 15")
	@Pattern(regexp = "\\w+( \\w+)*", message = "Illegal display name")
	private String displayName;
	
	@Username
	private String username;

	@Password
	private String password;
	
	@Password
	private String confirmedPassword;
	
	@Email(message = "Illegal email address")
	@NotNull(message = "Email is mandatory")
	private String email;
	
	@Email(message = "Illegal confirmed-email address")
	@NotNull(message = "Please type confirmed-email section")
	private String confirmedEmail;
	
	@Past
	@NotNull(message = "Date of birth is mandatory!")
	private LocalDate dateOfBirth;
}

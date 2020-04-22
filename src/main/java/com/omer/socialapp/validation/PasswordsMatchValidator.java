package com.omer.socialapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.omer.socialapp.model.UserRegistrationRequestParams;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationRequestParams>
{
	
	@Override
    public void initialize(PasswordsMatch constraint) {
		
    }
	 
	 @Override
	 public boolean isValid(UserRegistrationRequestParams target, ConstraintValidatorContext context) {
		 if(target.getPassword() == null || target.getConfirmedPassword() == null)
			 return false;
		 
		 return target.getPassword().equals(target.getConfirmedPassword());
	 }
}

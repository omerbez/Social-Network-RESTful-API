package com.omer.socialapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.omer.socialapp.model.UserRegistrationRequestParams;

public class EmailsMatchValidator implements ConstraintValidator<EmailsMatch, UserRegistrationRequestParams>
{
	
	@Override
    public void initialize(EmailsMatch constraint) {
		
    }
	 
	 @Override
	 public boolean isValid(UserRegistrationRequestParams target, ConstraintValidatorContext context) {
		 if(target.getEmail() == null || target.getConfirmedEmail() == null)
			 return false;
		 
		 return target.getEmail().equals(target.getConfirmedEmail());
	 }
}

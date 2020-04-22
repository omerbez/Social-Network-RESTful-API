package com.omer.socialapp.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordValidator implements ConstraintValidator<Password, String>
{
	private int min, max;
			
	@Override
	public void initialize(Password constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.length() < min || value.length() > max)
			return false;
		
		Pattern p = Pattern.compile("[A-Za-z0-9]+");
		if(!p.matcher(value).matches())
			return false;
		
		boolean containsLetters = false;
		boolean containsDigits = false;
		for(int i=0; i<value.length(); i++) {
			if((value.charAt(i) >= 'a' && value.charAt(i) <= 'z') || 
					(value.charAt(i) >= 'A' && value.charAt(i) <= 'Z'))
				containsLetters = true;
			
			if(value.charAt(i) >= '0' && value.charAt(i) <= '9')
				containsDigits = true;
			
			if(containsDigits && containsLetters)
				break;
		}
		
		return containsDigits && containsLetters;
	}
}
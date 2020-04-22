package com.omer.socialapp.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UsernameValidator implements ConstraintValidator<Username, String>
{
	private int min, max;
			
	@Override
	public void initialize(Username constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.length() < min || value.length() > max)
			return false;
		
		Pattern p = Pattern.compile("\\w+");
		return p.matcher(value).matches();
	}
}
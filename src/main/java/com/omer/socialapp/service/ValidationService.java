package com.omer.socialapp.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ValidationService 
{
	
	
	public String processBindingErrors(BindingResult bindingResult) {
		if(bindingResult == null || !bindingResult.hasErrors())
			return "No Errors";
		
		StringBuilder builder = new StringBuilder("Validation Error: ");
		String errors = bindingResult.getAllErrors()
							.stream()
							.map(objectError -> objectError.getDefaultMessage())
							.collect(Collectors.joining(", "));
		
		builder.append(errors);
		return builder.toString();
	}
	
	
	@Constraint(validatedBy = PasswordValidator.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Password {
		
		public String message() default "Illegal password";
		
		public int min() default 6;
		
		public int max() default 12;
		
		Class<?>[] groups() default {};
		Class<? extends Payload>[] payload() default {};
	}
	
	// class must be public
	public static class PasswordValidator implements ConstraintValidator<Password, String>
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
			
			Pattern p = Pattern.compile("([A-Za-z]+[0-9]+)*([0-9]+[A-Za-z]+)*");
			return p.matcher(value).matches();
		}
	}
}

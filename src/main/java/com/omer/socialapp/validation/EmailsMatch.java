package com.omer.socialapp.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EmailsMatchValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EmailsMatch {
	public String message() default "Emails does not match!";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}

package com.omer.socialapp.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Password {
	
	public String message() default "Illegal password";
	
	public int min() default 8;
	
	public int max() default 12;
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
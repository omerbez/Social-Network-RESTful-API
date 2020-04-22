package com.omer.socialapp.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;



// Define Username validation here for reusing it..
@Constraint(validatedBy = UsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Username {
	
	public String message() default "Illegal Username";
	
	public int min() default 4;
	
	public int max() default 12;
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
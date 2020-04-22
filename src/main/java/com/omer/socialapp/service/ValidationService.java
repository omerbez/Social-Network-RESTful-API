package com.omer.socialapp.service;

import java.util.stream.Collectors;

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
}
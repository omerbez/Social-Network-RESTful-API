package com.omer.socialapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.omer.socialapp.adapter.UserEntityModelAdapter;
import com.omer.socialapp.controller.UserController;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.UserRepository;
import com.omer.socialapp.service.UserService;


@SpringBootApplication
@ComponentScan(basePackageClasses = {User.class, UserRepository.class, UserController.class, 
		UserEntityModelAdapter.class, UserNotFoundException.class, UserService.class})
public class RestSocialAppFinalProjectApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(RestSocialAppFinalProjectApplication.class, args);
	}

}

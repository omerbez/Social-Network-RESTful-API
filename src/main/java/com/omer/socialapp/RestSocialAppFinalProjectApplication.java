package com.omer.socialapp;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
	
	@Bean 
	public CommandLineRunner init(UserRepository userRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
//				User user1 = new User("Omer Bezalel", "user123", "pass123", "email@gmail.com", 28);	
//				userRepository.save(user1);
			}
		};
	}
}

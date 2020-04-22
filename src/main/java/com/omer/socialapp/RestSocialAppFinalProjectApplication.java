package com.omer.socialapp;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.omer.socialapp.adapter.UserEntityModelAdapter;
import com.omer.socialapp.controller.UserController;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.UserRepository;
import com.omer.socialapp.security.Roles;
import com.omer.socialapp.security.SecurityConfig;
import com.omer.socialapp.service.UserService;


@SpringBootApplication
@ComponentScan(basePackageClasses = {RestSocialAppFinalProjectApplication.class, User.class, UserRepository.class, UserController.class, 
		UserEntityModelAdapter.class, UserNotFoundException.class, UserService.class, SecurityConfig.class})
public class RestSocialAppFinalProjectApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(RestSocialAppFinalProjectApplication.class, args);
	}
	
	@Bean 
	public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				if(!userRepository.existsByUsername("admin")) {
					User admin = new User("Omer Bezalel", "admin", "pass1234", "email@gmail.com", LocalDate.of(1992, 4, 25), 
							Arrays.asList(Roles.ADMIN.getValue()), passwordEncoder);
					
					userRepository.save(admin);					
				}
			}
		};
	}
}

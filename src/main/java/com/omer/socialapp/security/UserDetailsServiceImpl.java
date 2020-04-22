package com.omer.socialapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.omer.socialapp.repository.UserRepository;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
    private UserRepository userRepository;
    
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findByUsername(username).orElseThrow(() ->
        			new UsernameNotFoundException("Username: " + username + " not found")));
    }
}
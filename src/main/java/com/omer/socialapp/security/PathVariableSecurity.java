package com.omer.socialapp.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.UserRepository;


/**
 * Check whether the user asks for his resource (and not others resources..)
 * Secure the @PathVariables of the Controllers..
 */
@Component
public class PathVariableSecurity 
{
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Transactional(readOnly=true, noRollbackFor=Exception.class)
    public boolean checkUserId(Authentication authentication, long id) {
    	// get the username from the request
        String username = authentication.getName();
        User result = userRepository.findByUsername(username).orElse(null);

        //check if username exists + the id is match to the {uid} PathVariable
        return result != null && result.getId() == id; 
    }
    
    @Transactional(readOnly=true, noRollbackFor=Exception.class)
    public boolean checkIfLikePage(Authentication authentication, long pageId) {
    	String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && userOptional.get().isLikePage(pageId);
    }
    
    @Transactional(readOnly=true, noRollbackFor=Exception.class)
    public boolean checkIfInGroup(Authentication authentication, long groupId) {
    	String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && userOptional.get().isRegisteredIn(groupId);
    }
    
    @Transactional(readOnly=true, noRollbackFor=Exception.class)
    public boolean checkIfPageOwner(Authentication authentication, long pageId) {
    	String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && userOptional.get().isOwnerOfPage(pageId);
    }
    
    @Transactional(readOnly=true, noRollbackFor=Exception.class)
    public boolean checkIfGroupOwner(Authentication authentication, long groupId) {
    	String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && userOptional.get().isOwnerOfGroup(groupId);
    }
}

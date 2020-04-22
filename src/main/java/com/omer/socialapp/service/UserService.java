package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.omer.socialapp.adapter.UserEntityModelAdapter;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IUserLinksMethods;
import com.omer.socialapp.model.User;
import com.omer.socialapp.model.UserRegistrationRequestParams;
import com.omer.socialapp.repository.GroupRepository;
import com.omer.socialapp.repository.PageRepository;
import com.omer.socialapp.repository.UserRepository;


@Service
public class UserService implements IUserService
{
	private final UserRepository userRepository;
	private final UserEntityModelAdapter userModelAdapter;
	private final PageRepository pageRepository;
	private final GroupRepository groupRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public UserService(UserRepository repo, UserEntityModelAdapter modelAdapter,
			PageRepository pageRepo, GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
		userRepository = repo;
		userModelAdapter = modelAdapter;
		pageRepository = pageRepo;
		this.groupRepository = groupRepository;
		this.passwordEncoder = passwordEncoder;
	}

	
	@Override
	public User addUser(UserRegistrationRequestParams userForm) {
		User user = User.createFrom(userForm, passwordEncoder);
		
		// JPA validation..
		return userRepository.save(user);
	}

	@Override
	public CollectionModel<EntityModel<IUserLinksMethods>> toCollectionModel(Iterable<? extends IUserLinksMethods> users) {
		return userModelAdapter.toCollectionModel(users); 
	}

	@Override
	public EntityModel<IUserLinksMethods> toEntityModel(IUserLinksMethods user) {
		return userModelAdapter.toModel(user);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public boolean isExists(long uid) {
		return userRepository.existsById(uid);
	}

	@Override
	public void deleteUser(long uid) {
		if(!userRepository.existsById(uid))
			throw new IllegalArgumentException("Delete operation faild, User "+uid+" doesn't exists");
		
		userRepository.deleteById(uid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User updateUser(User existsUser, User newUser) {
		Assert.noNullElements(new Object[] {existsUser, newUser}, "Illegal user info");
		
		// if the user wants to change username
		if(newUser.getUsername() != existsUser.getUsername()) {
			User user = userRepository.findByUsername(newUser.getUsername()).orElse(null);
			//user with same username exists and it's not the current exists user (different id)
			if(user != null && user.getId() != existsUser.getId())
				throw new IllegalArgumentException("Username "+newUser.getUsername()+" already exists!");
			
			existsUser.setUsername(newUser.getUsername());
		}


		existsUser.setDisplayName(newUser.getDisplayName());
		existsUser.setDateOfBirth(newUser.getDateOfBirth());
		existsUser.setPassword(newUser.getPassword());
		existsUser.setEmail(newUser.getEmail());
		
		return existsUser;
	}
	
	@Override
	public User updateUser(long existsUserId, User newUser) {
		User existsUser = userRepository.findById(existsUserId).orElseThrow(() -> 
				new IllegalArgumentException("User "+existsUserId+" doesn't exists!"));
		
		return updateUser(existsUser, newUser);
	}
	

	@Override
	public User updateUser(long existsUserId, UserRegistrationRequestParams newUser) {
		return updateUser(existsUserId, User.createFrom(newUser, passwordEncoder));
	}

	@Override
	public boolean checkIfFriends(long uid1, long uid2) {
		if(uid1 == uid2 || userRepository.checkIfFriends(uid1, uid2))
			return true;
		
		return false;
	}

	/**
	 * return all pages which this user liked
	 */
	@Override
	public List<AbstractPage> getUserPages(long uid) {
		User user = userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		return pageRepository.findByLikedUsers(user);
	}

	@Override
	public List<Group> getUserGroups(long uid) {
		User user = userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
		return groupRepository.findByGroupUsers(user);
	}

	@Override
	public List<User> getAllUsers(String name, Integer age) {
		if(name == null && age == null)
			return getAllUsers();
		
		if(name != null && age != null)
			return userRepository.findByDisplayNameContainingAndAge(name, age);
		
		if(name != null)
			return userRepository.findByDisplayNameContaining(name);
		
		return userRepository.findByAge(age);
	}
}

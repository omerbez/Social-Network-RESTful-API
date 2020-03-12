package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IUserLinksMethods;
import com.omer.socialapp.model.User;

public interface IUserService {
	public User addUser(User user);
	public List<User> getAllUsers();
	public Optional<User> findById(long id);
	public boolean isExists(long uid);
	public void deleteUser(long uid);
	public User updateUser(User existsUser, User newUser);
	public User updateUser(long existsUserId, User newUser);
	public boolean checkIfFriends(long uid1, long uid2);
	public List<AbstractPage> getUserPages(long uid); 
	public List<Group> getUserGroups(long uid);
	public CollectionModel<EntityModel<IUserLinksMethods>> toCollectionModel(Iterable<? extends IUserLinksMethods> users);
	public EntityModel<IUserLinksMethods> toEntityModel(IUserLinksMethods user);
}

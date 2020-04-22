package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IGroupLinksMethods;
import com.omer.socialapp.model.User;


public interface IGroupService {
	public Optional<Group> findById(long id);
	public Group addGroup(Group group, String username);
	public List<Group> getAllGroups();
	public List<Group> getAllGroups(String name, String descContains);
	public List<User> getUsersOfGroup(long groupId);
	public void removeGroup(long groupId);
	public CollectionModel<EntityModel<IGroupLinksMethods>> toCollectionModel(Iterable<? extends IGroupLinksMethods> pages);
	public EntityModel<IGroupLinksMethods> toEntityModel(IGroupLinksMethods page);
}

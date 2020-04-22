package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omer.socialapp.adapter.GroupEntityModelAdapter;
import com.omer.socialapp.exceptions.GroupNotFoundException;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.IGroupLinksMethods;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.GroupRepository;
import com.omer.socialapp.repository.UserRepository;


@Service
public class GroupService implements IGroupService
{
	private GroupRepository groupRepository;
	private GroupEntityModelAdapter groupEntityAdapter;
	private UserRepository userRepository;
	
	
	
	@Autowired
	public GroupService(GroupRepository groupRepo, GroupEntityModelAdapter modelAdapter, UserRepository userRepo) {
		groupRepository = groupRepo;
		groupEntityAdapter = modelAdapter;
		userRepository = userRepo;
	}
	
	@Override
	public Optional<Group> findById(long id) {
		return groupRepository.findById(id);
	}

	@Override
	public List<Group> getAllGroups() {
		return groupRepository.findAll();
	}

	@Override
	public CollectionModel<EntityModel<IGroupLinksMethods>> toCollectionModel(
			Iterable<? extends IGroupLinksMethods> groups) {
		return groupEntityAdapter.toCollectionModel(groups);
	}

	@Override
	public EntityModel<IGroupLinksMethods> toEntityModel(IGroupLinksMethods group) {
		return groupEntityAdapter.toModel(group);
	}

	@Override
	public Group addGroup(Group group, String username) {
		User owner = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		group.setOwnerUser(owner);
		return groupRepository.save(group);
	}

	@Override
	public List<User> getUsersOfGroup(long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(()-> new GroupNotFoundException(groupId));
		return userRepository.findByGroups(group);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeGroup(long groupId) {
		var group = findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
		
		// remove the connection between the users and the group
		Set<User> groupUsers = group.getGroupUsers();
		if(groupUsers != null) {
			for(User user : groupUsers) {
				user.removeGroup(group);
			}
		}
		// delete the group and cascade the operation to it's posts and comments
		groupRepository.delete(group);
	}

	@Override
	public List<Group> getAllGroups(String name, String descContains) {
		if(name == null && descContains == null)
			return getAllGroups();
		
		if(name != null && descContains != null)
			return groupRepository.findByGroupNameLikeAndDescriptionContaining(name, descContains);
		
		if(name != null)
			return groupRepository.findByGroupNameLike(name);
		
		return groupRepository.findByDescriptionContaining(descContains);
	}
}

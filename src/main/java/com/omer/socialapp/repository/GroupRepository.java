package com.omer.socialapp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.User;


public interface GroupRepository extends JpaRepository<Group, Long> {
	public List<Group> findByGroupUsers(User user);
}

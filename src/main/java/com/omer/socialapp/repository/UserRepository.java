package com.omer.socialapp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
	public User findByUsername(String username);
	public List<User> findByLikedPages(AbstractPage page);
	public List<User> findByGroups(Group group);
	public List<User> findByDisplayNameContaining(String name);
	public List<User> findByAge(Integer age);
	public List<User> findByDisplayNameContainingAndAge(String name, Integer age);
}

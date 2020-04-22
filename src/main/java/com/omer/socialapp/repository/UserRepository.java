package com.omer.socialapp.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.Group;
import com.omer.socialapp.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
	public Optional<User> findByUsername(String username);
	public List<User> findByLikedPages(AbstractPage page);
	public List<User> findByGroups(Group group);
	public List<User> findByDisplayNameContaining(String name);
	public List<User> findByAge(Integer age);
	public List<User> findByDisplayNameContainingAndAge(String name, Integer age);
	
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.username = ?1")
    public Boolean existsByUsername(String username);
}

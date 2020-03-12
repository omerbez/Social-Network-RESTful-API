package com.omer.socialapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omer.socialapp.model.AbstractPage;
import com.omer.socialapp.model.User;

public interface PageRepository extends JpaRepository<AbstractPage, Long> {
	public List<AbstractPage> findByLikedUsers(User user);
}

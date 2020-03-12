package com.omer.socialapp.repository;

import com.omer.socialapp.model.User;

// Custom methods which couldn't implemented via @Query 
// so we should implements them seperatly.
public interface UserCustomRepository {
	public boolean checkIfFriends(User a, User b);
	public boolean checkIfFriends(long uid1, long uid2);
}

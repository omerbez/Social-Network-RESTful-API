package com.omer.socialapp.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.omer.socialapp.model.User;

// Custom implemention, Spring will recognize the class 
// by the "Impl" name posix!
public class UserCustomRepositoryImpl implements UserCustomRepository
{
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public boolean checkIfFriends(User a, User b) {
		return checkIfFriends(a.getId(), b.getId());
	}

	@Override
	public boolean checkIfFriends(long uid1, long uid2) {
		Query q = em.createNativeQuery("SELECT count(*) FROM friends_table "
				+ "WHERE ((user_id = :uid1 and friend_id = :uid2) or (user_id = :uid2 and friend_id = :uid1))");
		q.setParameter("uid1", uid1);
		q.setParameter("uid2", uid2);
		
		Number result = (Number)q.getSingleResult();
		return result.intValue() != 0;
	}

}

package com.omer.socialapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omer.socialapp.model.AbstractPost;
import com.omer.socialapp.model.PostOfGroup;
import com.omer.socialapp.model.PostOfPage;

public interface PostRepository extends JpaRepository<AbstractPost, Long> {
	
	//posts table is a Single-Table join type..
	@Query(nativeQuery = true,
	value = "SELECT * FROM posts WHERE post_type=\""+PostOfPage.DISCRIMINATOR_VALUE+"\" AND page_id=:pageId AND user_id=:userId")
	public List<PostOfPage> getUserPostsOfPage(long userId, long pageId);
	
	@Query(nativeQuery = true,
	value = "SELECT * FROM posts WHERE post_type=\""+PostOfGroup.DISCRIMINATOR_VALUE+"\" AND group_id=:groupId AND user_id=:userId")
	public List<PostOfGroup> getUserPostsOfGroup(long userId, long groupId);
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM posts WHERE post_type=\""+PostOfPage.DISCRIMINATOR_VALUE+"\" AND page_id=:pageId")
	public List<PostOfPage> getPostsOfPage(long pageId);
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM posts WHERE post_type=\""+PostOfGroup.DISCRIMINATOR_VALUE+"\" AND group_id=:groupId")
	public List<PostOfGroup> getPostsOfGroup(long groupId);
	
	@Query(nativeQuery = true,
			value = "SELECT post_type from posts WHERE id=:postId")
	public String getPostType(long postId);
	
	@Query(nativeQuery = true,
			value = "SELECT page_id from posts WHERE id=:postId")
	public Optional<Long> getPageIdOfPost(long postId);
	
	@Query(nativeQuery = true,
			value = "SELECT group_id from posts WHERE id=:postId")
	public Optional<Long> getGroupIdOfPost(long postId);
}

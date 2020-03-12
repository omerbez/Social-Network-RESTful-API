package com.omer.socialapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omer.socialapp.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	public List<Comment> findByRelatedPost_Id(long postId);
	public List<Comment> findByCommentedUser_Id(long userId);
}

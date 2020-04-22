package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.omer.socialapp.adapter.CommentEntityModelAdapter;
import com.omer.socialapp.exceptions.GeneralException;
import com.omer.socialapp.exceptions.PostNotFoundException;
import com.omer.socialapp.exceptions.UserNotFoundException;
import com.omer.socialapp.model.AbstractPost;
import com.omer.socialapp.model.Comment;
import com.omer.socialapp.model.ICommentLinksMethods;
import com.omer.socialapp.model.PostOfGroup;
import com.omer.socialapp.model.PostOfPage;
import com.omer.socialapp.model.RawComment;
import com.omer.socialapp.model.User;
import com.omer.socialapp.repository.CommentRepository;
import com.omer.socialapp.repository.PostRepository;
import com.omer.socialapp.repository.UserRepository;

@Service
public class CommentService implements ICommentService 
{
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentEntityModelAdapter commentModelAdapter;
	
	
	@Autowired
	public CommentService(CommentRepository commentRepository, CommentEntityModelAdapter commentModelAdapter,
			UserRepository userRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.commentModelAdapter = commentModelAdapter;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Optional<Comment> getCommentById(long id) {
		return commentRepository.findById(id);
	}

	@Override
	public Comment addComment(long postId, RawComment rawComment, String username) {
		Assert.noNullElements(new Object[] {rawComment, username, rawComment.getText()}, 
				"Illegal request arguments");
		Assert.hasLength(rawComment.getText(), "Comment text must contain al least 1 character");
			
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));		
		AbstractPost post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		
		// Check if user is allowed to comment on the post..
		if(postRepository.getPostType(postId).equals(PostOfPage.DISCRIMINATOR_VALUE)) {
			PostOfPage pagePost = (PostOfPage)post;
			Set<User> registeredUsers = pagePost.getRelatedPage().getLikedUsers();
			if(!registeredUsers.contains(user))
				throw new GeneralException("User "+user.getId()+" is not registered to the page so he can't comment on the desired post");
		} else {
			PostOfGroup groupPost = (PostOfGroup)post;
			Set<User> registeredUsers = groupPost.getRelatedGroup().getGroupUsers();
			if(!registeredUsers.contains(user))
				throw new GeneralException("User "+user.getId()+" is not registered to the group so he can't comment on the desired post");
		}
		
		return commentRepository.save(new Comment(rawComment.getText(), post, user));
	}

	@Override
	public EntityModel<ICommentLinksMethods> toEntityModel(ICommentLinksMethods comment) {
		return commentModelAdapter.toModel(comment);
	}

	@Override
	public CollectionModel<EntityModel<ICommentLinksMethods>> toCollectionModel(
			Iterable<? extends ICommentLinksMethods> comments) {
		return commentModelAdapter.toCollectionModel(comments);
	}

	@Override
	public List<Comment> getCommentsOfPost(long postId) {
		if(!postRepository.existsById(postId))
			throw new PostNotFoundException(postId);
		
		return commentRepository.findByRelatedPost_Id(postId);
	}

	@Override
	public List<Comment> getCommentsOfUser(long userId) {
		if(!userRepository.existsById(userId))
			throw new UserNotFoundException(userId);
		
		return commentRepository.findByCommentedUser_Id(userId);
	}
}

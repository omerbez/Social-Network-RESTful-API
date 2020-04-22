package com.omer.socialapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.omer.socialapp.model.Comment;
import com.omer.socialapp.model.ICommentLinksMethods;
import com.omer.socialapp.model.RawComment;

public interface ICommentService {
	public List<Comment> getCommentsOfPost(long postId);
	public List<Comment> getCommentsOfUser(long userId);
	public Optional<Comment> getCommentById(long id);
	public Comment addComment(long postId, RawComment rawComment, String username);
	public EntityModel<ICommentLinksMethods> toEntityModel(ICommentLinksMethods comment);
	public CollectionModel<EntityModel<ICommentLinksMethods>> toCollectionModel(Iterable<? extends ICommentLinksMethods> comments);
}

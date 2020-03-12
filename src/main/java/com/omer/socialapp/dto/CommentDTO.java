package com.omer.socialapp.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.omer.socialapp.model.Comment;
import com.omer.socialapp.model.ICommentLinksMethods;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

//change the default name in CollectionModel and EntityModel (the response JSON key name)
//the default is classnameList..
@Relation(collectionRelation = "commentsList", itemRelation = "comment") 
@Value
@JsonPropertyOrder(value = {"id", "commentedUserId", "relatedPostId", "text", "commentedTime"})
public class CommentDTO implements ICommentLinksMethods 
{
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private Comment comment;
	
	public CommentDTO(Comment comment) {
		this.comment = comment;
	}
	
	@Override
	public Long getId() {
		return comment.getId();
	}
	
	public String getText() {
		return comment.getText();
	}
	
	public LocalDateTime getCommentedTime() {
		return comment.getCreationTime();
	}
	
	@Override
	public Long getRelatedPostId() {
		return comment.getRelatedPost().getId();
	}
	
	public Long getCommentedUserId() {
		return comment.getCommentedUser().getId();
	}
}

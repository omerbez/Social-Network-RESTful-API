package com.omer.socialapp.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.omer.socialapp.model.AbstractPost;
import com.omer.socialapp.model.IPostLinksMethods;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

//change the default name in CollectionModel and EntityModel (the response JSON key name)
//the default is classnameList..
@Relation(collectionRelation = "postsList", itemRelation = "post") 
@Value
@JsonPropertyOrder(value = {"postId", "text", "postedTime", "postedUserId"})
public class PostDTO implements IPostLinksMethods 
{
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private AbstractPost post;
	
	@JsonIgnore
	private long subjectId; // page or group id..
	
	
	public PostDTO(AbstractPost post, long subjectId) {
		this.post = post;
		this.subjectId = subjectId;
	}
	
	public String getText() {
		return post.getText();
	}
	
	public LocalDateTime getPostedTime() {
		return post.getPostedTime();
	}

	@Override
	public Long getPostId() {
		return post.getId();
	}

	@Override
	@JsonProperty("postedUserId")
	public Long getUserId() {
		return post.getPostedUser().getId();
	}

	@Override
	@JsonIgnore
	public Long getSubjectId() {
		return subjectId;
	}
}

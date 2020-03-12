package com.omer.socialapp.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor //For Hibernate
public class Comment implements ICommentLinksMethods
{
	@Id @GeneratedValue
	private Long id;
	
	private String text;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(AccessLevel.NONE)
	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private AbstractPost relatedPost;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(AccessLevel.NONE)
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User commentedUser;
	
	private final LocalDateTime creationTime = LocalDateTime.now();
	
	
	public Comment(String text, AbstractPost ofPost, User ofUser) {
		this.text = text;
		relatedPost = ofPost;
		commentedUser = ofUser;
	}

	@Override
	@JsonIgnore
	public Long getRelatedPostId() {
		return relatedPost.getId();
	}
}

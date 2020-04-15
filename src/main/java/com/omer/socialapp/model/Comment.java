package com.omer.socialapp.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Post text must have at least 1 character")
	@Size(max = 250, message = "Post is to long (max length is 250)")
	@Column(nullable = false)
	private String text;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(AccessLevel.NONE)
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "post_id", nullable = false)
	private AbstractPost relatedPost;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(AccessLevel.NONE)
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "user_id", nullable = false)
	private User commentedUser;
	
	@Column(name = "creation_time", nullable = false)
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

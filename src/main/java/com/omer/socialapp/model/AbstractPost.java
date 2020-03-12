package com.omer.socialapp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity(name = "posts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="post_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class AbstractPost 
{
	@Id @GeneratedValue
	private Long id;
	
	private String text;
	
	private final LocalDateTime postedTime = LocalDateTime.now();
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private User postedUser;
	
	@Setter(AccessLevel.NONE)
	@OneToMany(mappedBy = "relatedPost")
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private List<Comment> comments;
	
	
	public AbstractPost(String text, User postedUser) {
		this.text = text;
		this.postedUser = postedUser;
	}
	
	public void addComment(Comment c) {
		comments.add(c);
	}
}

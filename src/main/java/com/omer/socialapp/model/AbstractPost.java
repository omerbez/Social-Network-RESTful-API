package com.omer.socialapp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Post text must have at least 1 character")
	@Size(max = 250, message = "Post is to long (max length is 250)")
	@Column(nullable = false)
	private String text;
	
	@Column(name = "posted_time", nullable = false)
	private final LocalDateTime postedTime = LocalDateTime.now();
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "user_id", nullable = false)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private User postedUser;
	
	@Setter(AccessLevel.NONE)
	@OneToMany(mappedBy = "relatedPost", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private List<Comment> comments;

	
	public AbstractPost(String text, User postedUser) {
		this.text = text;
		this.postedUser = postedUser;
	}
	
	public void addComment(Comment c) {
		comments.add(c);
	}
	
	/**
	 * @return the associate Page/Group ID
	 */
	public abstract Long getSubjectId();
}

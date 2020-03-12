package com.omer.socialapp.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class AbstractPage implements IPageLinksMethods
{	
	@Id @GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	private String name;
	
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	private String description;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@ManyToMany(mappedBy = "likedPages")
	private Set<User> likedUsers;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@OneToMany(mappedBy = "relatedPage")
	private Set<PostOfPage> pagePosts;
	
	
	public AbstractPage(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	AbstractPage() {
		// Default constructor for Hibernate and Jackson deserialize
	}
}

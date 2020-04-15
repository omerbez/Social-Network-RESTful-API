package com.omer.socialapp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class AbstractPage implements IPageLinksMethods
{	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Page name is mandatory")
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	@Size(min = 4, max = 25, message = "Page name length must be between 4 to 25")
	@Pattern(regexp = "\\w+( \\w)*", message = "Illegal page name")
	private String name;
	
	
	@NotNull
	@Size(max = 100, message = "Description length must not exceed 100 characters")
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	private String description = "";  // default value for the field..
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@ManyToMany(mappedBy = "likedPages", cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	private Set<User> likedUsers;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@OneToMany(mappedBy = "relatedPage", cascade = CascadeType.ALL)
	private Set<PostOfPage> pagePosts;
	
	
	public AbstractPage(String name, String description) {
		this.name = name;
		if(description != null)
			this.description = description;
	}
	
	AbstractPage() {
		// Default constructor for Hibernate and Jackson deserialize
	}
}

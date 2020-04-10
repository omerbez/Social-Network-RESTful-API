package com.omer.socialapp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "groups_table") // the name "group" and "groups" are reversed names of MySQL..
@Data
@NoArgsConstructor //For Hibernate
public class Group 
{	
	@Id @GeneratedValue
	private Long id;
	
	@EqualsAndHashCode.Exclude
	private String groupName;
	
	@EqualsAndHashCode.Exclude
	private String description;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(value = AccessLevel.NONE)
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups", cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	private Set<User> groupUsers;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "relatedGroup", cascade = CascadeType.ALL)
	private Set<PostOfGroup> groupPosts;
	
	
	public Group(String groupName, String description) {
		this.groupName = groupName;
		this.description = description;
	}
}

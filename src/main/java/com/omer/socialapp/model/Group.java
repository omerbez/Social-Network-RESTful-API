package com.omer.socialapp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Group name is mandatory")
	@Size(min = 4, max = 12, message = "Group name length must be between 4 to 12")
	@Pattern(regexp = "\\w+( \\w)*", message = "Illegal group name")
	@EqualsAndHashCode.Exclude
	@Column(name = "group_name", nullable = false)
	private String groupName;
	
	@EqualsAndHashCode.Exclude
	@Column(nullable = false)
	private String description = ""; // default value..
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Setter(value = AccessLevel.NONE)
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups", cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	private Set<User> groupUsers;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "relatedGroup", cascade = CascadeType.ALL)
	private Set<PostOfGroup> groupPosts;
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "owner_user", nullable = false)
	private User ownerUser;
	
	
	public Group(String groupName, String description) {
		this.groupName = groupName;
		if(description != null)
			this.description = description;
	}
	
	public String getOwnerUsername() {
		return ownerUser.getUsername();
	}
}

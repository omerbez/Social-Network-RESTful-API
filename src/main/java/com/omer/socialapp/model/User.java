package com.omer.socialapp.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class User implements IUserLinksMethods
{
	@Id @GeneratedValue
	private Long id;
	
	private String displayName;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String email;
	
	private Integer age;
	
	private final LocalDateTime creationDate = LocalDateTime.now();
	
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="friends_table",
	   joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
	   inverseJoinColumns = @JoinColumn(name="friend_id", referencedColumnName="id"))
	private Set<User> addedFriendsList;
	
	//recursice relationship, addedFriends = friends which this user invited them (=== this user follow them),
	// addedByFriendsList = friends which invited this user (=== users which folows this user..)
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@ManyToMany(mappedBy = "addedFriendsList", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	private Set<User> addedByFriendsList;
	
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@OneToMany(mappedBy = "postedUser", cascade = CascadeType.ALL)
	private Set<AbstractPost> posts;
	
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="pages_likes",
	   joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
	   inverseJoinColumns = @JoinColumn(name="page_id", referencedColumnName="id"))
	private Set<AbstractPage> likedPages;
	
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="users_groups", 
	   joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
	   inverseJoinColumns = @JoinColumn(name="group_id", referencedColumnName="id"))
	private Set<Group> groups;
	
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@OneToMany(mappedBy = "commentedUser", cascade = CascadeType.ALL)
	private Set<Comment> comments;
	
	
	public User(String displayname, String username, String password, String email, int age) {
		Assert.noNullElements(new Object[] {displayname, username, password}, "Null values are illegal");
		this.displayName = displayname;
		this.username = username;
		this.password = password;
		this.email = email;
		this.age = age;
		addedFriendsList = new HashSet<>();
		addedByFriendsList = new HashSet<>();
	}
	
	
	public void addFriend(User friend) {
		assert friend != null;
		addedFriendsList.add(friend);
	}
	
	public void removeFriend(User friend) {
		assert friend != null;
		addedFriendsList.remove(friend);
		addedByFriendsList.remove(friend);
	}
	
	@JsonIgnore
	public Set<User> getUserFriends() {
		Set<User> result = new HashSet<>();
		addedFriendsList.forEach(user -> result.add(user));
		for(User u : addedByFriendsList) 
			result.add(u);

		return result;
	}
	
	public void likePage(AbstractPage pageToLike) {
		assert pageToLike != null;
		likedPages.add(pageToLike);
	}
	
	public void unlikePage(AbstractPage pageToUnLike) {
		assert pageToUnLike != null;
		likedPages.remove(pageToUnLike);
	}
	
	public void removeGroup(Group group) {
		assert group != null;
		groups.remove(group);
	}
	
	public void addGroup(Group group) {
		assert group != null;
		groups.add(group);
	}
	
	public void addPost(AbstractPost post) {
		assert post != null;
		posts.add(post);
	}
	
	public boolean isRegisteredIn(AbstractPage page) {
		assert page != null;
		return likedPages.contains(page);
	}
	
	public boolean isRegisteredIn(Group group) {
		assert group != null;
		return groups.contains(group);
	}
	
	@PreRemove 
	//will be called before removed and with opened Transaction! so all changes  will applied to the DB
	private void removeFromFriends() {
		//remove the associate connections before delete
		addedFriendsList.forEach(user -> user.removeFriend(this));
		addedByFriendsList.forEach(user -> user.removeFriend(this));
		likedPages.clear();
		groups.clear();
	}
}

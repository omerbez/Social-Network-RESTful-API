package com.omer.socialapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.omer.socialapp.validation.Password;
import com.omer.socialapp.validation.Username;

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
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Display name is mandatory")
	@Column(name = "display_name", nullable = false)
	@Size(min = 3, max = 15, message = "Display name length must be 3 to 15")
	@Pattern(regexp = "\\w+( \\w+)*", message = "Illegal display name")
	@EqualsAndHashCode.Exclude
	private String displayName;
	
	@Username
	@Setter(value = AccessLevel.NONE)
	@Column(name = "username", nullable = false, unique = true)
	@EqualsAndHashCode.Exclude
	private String username;
	
	// The Hibernate validation will be on the confirmedPassword only - (because the real password
	// is encrypted..) the presentation layer will validate the entire User (by decoupling the User Entity
	// from the registration form) - separate class UserRegistrationRequestParams.
	@Column(name = "password", nullable = false)
	@EqualsAndHashCode.Exclude
	private String password;
	
	@Transient
	@Password
	@EqualsAndHashCode.Exclude
	@JsonProperty(access = Access.WRITE_ONLY) // deserialization only..
	private String confirmedPassword;
	
	@Column(nullable = false)
	@Email(message = "Illegal email address")
	@NotNull(message = "Email is mandatory")
	@EqualsAndHashCode.Exclude
	private String email;
	
	@Column(name = "date_of_birth", nullable = false)
	@Past
	@NotNull(message = "Date of birth is mandatory!")
	@EqualsAndHashCode.Exclude
	private LocalDate dateOfBirth;
	
	@Column(name = "creation_date", nullable = false)
	@EqualsAndHashCode.Exclude
	private final LocalDateTime creationDate = LocalDateTime.now();
	
	@JsonIgnore()
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@Setter(AccessLevel.NONE)
	// calculated from dateOfBirth, should also be in the DB for queries pruporse..
	private int age;  
	
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
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@OneToMany(mappedBy = "postedUser", cascade = CascadeType.ALL)
	private Set<AbstractPost> posts;
	
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="pages_likes",
	   joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
	   inverseJoinColumns = @JoinColumn(name="page_id", referencedColumnName="id"))
	private Set<AbstractPage> likedPages;
	

	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@OneToMany(mappedBy = "ownerUser", cascade = CascadeType.ALL)
	private Set<AbstractPage> ownedPages;
	
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, 
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="users_groups", 
	   joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
	   inverseJoinColumns = @JoinColumn(name="group_id", referencedColumnName="id"))
	private Set<Group> groups;
	
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@OneToMany(mappedBy = "ownerUser", cascade = CascadeType.ALL)
	private Set<Group> ownedGroups;
	
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@OneToMany(mappedBy = "commentedUser", cascade = CascadeType.ALL)
	private Set<Comment> comments;
	
	/**
	 * User Roles and Authorities
	 */
	@JsonIgnore
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name =  "user_roles", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
	@Column(name = "role") // the name in the other table - "user_roles"
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private List<String> roles = new ArrayList<>();
	
	@JsonIgnore
	@Column(name = "activated", nullable = false)
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
    private boolean accountActive = true;
    
	
	public User(String displayname, String username, String password, String email, LocalDate dateOfBirth, 
			List<String> roles, PasswordEncoder passwordEncoder) {
		Assert.noNullElements(new Object[] {displayname, username, password, email, dateOfBirth, roles, passwordEncoder},
				"One or more of the user's fields are missing!");
		
		this.displayName = displayname;
		this.username = username;
		this.password = passwordEncoder.encode(password);
		this.confirmedPassword = password;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.roles = roles;
		
		// calculate the age..
		Period p = Period.between(dateOfBirth, LocalDate.now());
		age = p.getYears();
	}
	
	/**
	 * Creates a regular ready-to-persist User entity - with a normal authorities (no roles)
	 * @param userForm validated params - must not be null.
	 * @param passwordEncoder the password encoder - must not be null.
	 */
	public static User createFrom(UserRegistrationRequestParams userForm, PasswordEncoder passwordEncoder) {
		// simple factory method - in order to have logic/validations before delefation the creation to the constructor
		assert userForm != null && passwordEncoder != null;
		return new User(userForm.getDisplayName(), userForm.getUsername(), userForm.getPassword(), userForm.getEmail(),
				userForm.getDateOfBirth(), new ArrayList<>(), passwordEncoder);
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
	
	public boolean isLikePage(AbstractPage page) {
		assert page != null;
		return isLikePage(page.getId());
	}
	
	public boolean isRegisteredIn(Group group) {
		assert group != null;
		return isRegisteredIn(group.getId());
	}
	
	public boolean isRegisteredIn(long groupId) {
		
		for(Group group : groups) {
			if(group.getId().longValue() == groupId)
				return true;
		}
		
		for(Group group : ownedGroups) {
			if(group.getId().longValue() == groupId)
				return true;
		}
		
		return false;
	}
	
	public boolean isLikePage(long pageId) {
		if(likedPages == null)
			return false;
		
		for(AbstractPage page : likedPages) {
			if(page.getId().longValue() == pageId)
				return true;
		}
		
		for(AbstractPage page : ownedPages) {
			if(page.getId().longValue() == pageId)
				return true;
		}
		
		return false;
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
	
	public boolean isAccountActive() {
		return accountActive;
	}
	
	public boolean isOwnerOfGroup(long groupId) {
		for(Group group : ownedGroups) {
			if(group.getId() == groupId)
				return true;
		}
		return false;
	}
	
	public boolean isOwnerOfPage(long pageId) {
		for(AbstractPage page : ownedPages) {
			if(page.getId() == pageId)
				return true;
		}
		return false;
	}
}

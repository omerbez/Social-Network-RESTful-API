package com.omer.socialapp.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;



@Entity
@DiscriminatorValue(value = PostOfGroup.DISCRIMINATOR_VALUE)
@NoArgsConstructor //For Hibernate..
public class PostOfGroup extends AbstractPost implements IPostLinksMethods
{
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonIgnore
	@JoinColumn(name = "group_id")
	private Group relatedGroup;
	
	public static final String DISCRIMINATOR_VALUE = "GROUP_POST";
	
	
	public PostOfGroup(String text, User postedUser, Group relatedGroup) {
		super(text, postedUser);
		this.relatedGroup = relatedGroup;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public Long getPostId() {
		return getId();
	}

	@Override
	public Long getUserId() {
		return getPostedUser().getId();
	}

	@Override
	public Long getSubjectId() {
		return relatedGroup.getId();
	}
	
	public Group getRelatedGroup() {
		return relatedGroup;
	}
}

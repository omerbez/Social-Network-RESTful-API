package com.omer.socialapp.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;



@Entity
@DiscriminatorValue(value = PostOfPage.DISCRIMINATOR_VALUE)
@NoArgsConstructor //For Hibernate..
public class PostOfPage extends AbstractPost implements IPostLinksMethods
{
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "page_id")
	@JsonIgnore
	private AbstractPage relatedPage;
	
	public static final String DISCRIMINATOR_VALUE = "PAGE_POST";
	
	
	public PostOfPage(String text, User postedUser, AbstractPage relatedPage) {
		super(text, postedUser);
		this.relatedPage = relatedPage;
	}

	@JsonIgnore
	@Override
	public Long getPostId() {
		return getId();
	}

	@JsonIgnore
	@Override
	public Long getUserId() {
		return getUserId();
	}

	@JsonIgnore
	@Override
	public Long getSubjectId() {
		return relatedPage.getId();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	public AbstractPage getRelatedPage() {
		return relatedPage;
	}
}

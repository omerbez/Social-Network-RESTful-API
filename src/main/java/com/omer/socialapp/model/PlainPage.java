package com.omer.socialapp.model;

import javax.persistence.Entity;


@Entity
public class PlainPage extends AbstractPage
{
	@SuppressWarnings("unused")
	private PlainPage() {
		// Default constructor for Hibernate and Jackson deserialize
	}
	
	public PlainPage(String name, String description, User owner) {
		super(name, description, owner);
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

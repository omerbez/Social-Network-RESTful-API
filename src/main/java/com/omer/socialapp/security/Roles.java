package com.omer.socialapp.security;

public enum Roles {
	ADMIN("ROLE_ADMIN");
	
	private String value;
	private String nonPrefixName;
	
	private Roles(String value) {
		this.value = value;
		nonPrefixName = value.replace("ROLE_", "");
	}
	
	/**
	 * get the complite role value - that has "ROLE_" prefix.
	 * for example "ROLE_USER"
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * get the role name - that is without the "ROLE_" prefix,
	 * for example "USER"
	 */
	public String getRoleName() {
		return nonPrefixName;
	}
}

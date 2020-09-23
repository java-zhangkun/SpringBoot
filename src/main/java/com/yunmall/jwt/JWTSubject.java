package com.yunmall.jwt;

public class JWTSubject {
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "JWTSubject [username=" + username + "]";
	}

	public JWTSubject(String username) {
		super();
		this.username = username;
	}

	public JWTSubject() {
		super();
	}
}

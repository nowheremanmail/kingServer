package com.dag.king.model;

/**
 * This class is to persist Token information per user
 * 
 * @author david.galindo
 *
 */
public class Token {

	private User user;
	private String token;
	private long creationTime;

	public Token(String tokenString, User userObj, long creationTimeInMillis) {
		token = tokenString;
		user = userObj;
		creationTime = creationTimeInMillis;
	}

	public User getUser() {
		return user;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public String getToken() {
		return token;
	}
}

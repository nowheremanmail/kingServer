package com.dag.king.model;

/**
 * POJO to store user info. Now just the ID
 * 
 * @author david.galindo
 *
 */
public class User {

	private int id;
	// we will store the last token generated
	private String token;

	public User(int userId) {
		id = userId;
	}

	public int getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String t) {
		token = t;
	}

}

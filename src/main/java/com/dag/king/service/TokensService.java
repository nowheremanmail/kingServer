package com.dag.king.service;

import com.dag.king.model.User;

/**
 * Responsible for all token sutff
 * 
 * @author david
 *
 */
public interface TokensService {

	/**
	 * given an user returns its valid token
	 * 
	 * @param user
	 * @return
	 */
	public String getTokenForUser (User user);
	
	/**
	 * given a token, returns a valid user only if exists a token and it's still valid (less than 10 minutes old)
	 * 
	 * @param token
	 * @return
	 */
	public User getUserFromToken (String token);
	
}

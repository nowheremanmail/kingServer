package com.dag.king.repository;

import com.dag.king.model.Token;
import com.dag.king.model.User;

/**
 * Responsible to persist Tokens for each user.
 * 
 * @author david.galindo
 *
 */
public interface TokensRepository {

	/**
	 * store token into repository, linked to given user serverTime must be
	 * current time
	 * 
	 * @param token
	 * @param user
	 */
	public void storeToken(String token, User user, long serverTime);

	/**
	 * Returns current Token object
	 * 
	 * @param token
	 * @param user
	 */
	public Token getUserFromToken(String token);

//	/**
//	 * remove given token from repository
//	 * 
//	 * @param token
//	 */
//	public void remove(String token);

	/**
	 * clean on expired token before given date
	 * 
	 * @param serverTime
	 */
	public void clean(long serverTime);

}

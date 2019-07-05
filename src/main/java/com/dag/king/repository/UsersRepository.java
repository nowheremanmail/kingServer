package com.dag.king.repository;

import com.dag.king.model.User;

/**
 * Responsible to keep users information at this moment it's just an ID, but it
 * could potentially store more information
 * 
 * @author david.galindo
 *
 */
public interface UsersRepository {

	/**
	 * Check if given userId exists on repository
	 * 
	 * @param userId
	 * @return
	 */
	boolean isValid(int userId);

	/**
	 * get existing user from repository
	 * 
	 * @param userId
	 * @return
	 */
	User getUser(int userId);

	/**
	 * Add a new user into repository returns null if for any reason user cannot
	 * be added
	 * 
	 * @param userId
	 * @return
	 */
	User addUser(int userId);
}

package com.dag.king.service;

import com.dag.king.model.User;

/**
 * Interface for User service functionality
 * 
 * @author david.galindo
 *
 */
public interface UsersService {
	/**
	 * Given an userId, returns existing user or new
	 * 
	 * @param userId
	 * @return
	 */
	User login(int userId);

}

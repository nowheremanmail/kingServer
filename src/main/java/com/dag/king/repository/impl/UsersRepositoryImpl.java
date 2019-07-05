package com.dag.king.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.User;
import com.dag.king.repository.UsersRepository;

/**
 * Persistency for User information
 * 
 * @author david.galindo
 *
 */
public class UsersRepositoryImpl implements UsersRepository {

	private static Logger logger = Logger.getLogger("UsersRepositoryImpl");

	private static final String SYNC = "SYNC";

	private Map<Integer, User> users;

	public UsersRepositoryImpl() {
		users = new HashMap<Integer, User>(1024*1024);
	}

	@Override
	public boolean isValid(int userId) {
		boolean res = users.get(userId) != null;

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("check user [ " + userId + "] = " + res);
		}

		return res;
	}

	@Override
	public User addUser(int userId) {
		User user = users.get(userId);
		if (user == null) {
			synchronized (SYNC) {
				user = users.get(userId);
				if (user == null) {
					if (logger.isLoggable(Level.FINEST)) {
						logger.finest("user [ " + userId + "] created");
					}

					user = new User(userId);
					users.put(userId, user);
				} else {
					if (logger.isLoggable(Level.FINEST)) {
						logger.finest("user [ " + userId + "] exists!");
					}
				}
			}
		} else {
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("user [ " + userId + "] exists");
			}
		}
		return user;
	}

	@Override
	public User getUser(int userId) {
		return users.get(userId);
	}

}

package com.dag.king.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.User;
import com.dag.king.repository.UsersRepository;
import com.dag.king.service.UsersService;

public class UsersServiceImpl implements UsersService {

	private static Logger logger = Logger.getLogger("UsersServiceImpl");

	private UsersRepository usersRepository;

	public UsersServiceImpl(UsersRepository repo) {
		usersRepository = repo;
	}

	@Override
	public User login(int userId) {
		//
		// if user doesn't exists, we will automatically create.
		//
		User user = usersRepository.getUser(userId);
		if (user == null) {
			user = usersRepository.addUser(userId);
			if (user == null) {
				// we log a message for future investigation
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("user [" + userId + "] cannot be created");
				}

				return null;
			}
			else {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("user [" + userId + "] created");
				}				
			}
		}

		return user;
	}
}

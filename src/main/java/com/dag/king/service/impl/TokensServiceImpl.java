package com.dag.king.service.impl;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.Token;
import com.dag.king.model.User;
import com.dag.king.repository.TokensRepository;
import com.dag.king.service.TokensService;

public class TokensServiceImpl implements TokensService {

	private static Logger logger = Logger.getLogger("TokensServiceImpl");
	private static final int EVERY_MINUTE = 60 * 1000;
	private static final int TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000;

	private TokensRepository tokensRepository;

	private Timer expireTimer;

	public TokensServiceImpl(TokensRepository repo) {
		tokensRepository = repo;

		// process to clean expired tokens, we will check every minute
		expireTimer = new Timer(true);
		expireTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				tokensRepository.clean(System.currentTimeMillis() - TEN_MINUTES_IN_MILLIS);

			}
		}, EVERY_MINUTE, EVERY_MINUTE);
	}

	@Override
	public String getTokenForUser(User user) {
		
		// for each login we will need a 10 minutes valid token
		// then always we have to generate a new one
		//
		// to make as unique as possible and avoid collisions, we will user time and saome random plus userId
		String token = Long.toUnsignedString(System.currentTimeMillis(), Character.MAX_RADIX) + Long.toUnsignedString(ThreadLocalRandom.current().nextLong(), Character.MAX_RADIX) + Integer.toUnsignedString(user.getId(), Character.MAX_RADIX);

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("new token [ " + token + "] for user [" + user.getId() + "]");
		}
	
		long serverTime = System.currentTimeMillis();

		tokensRepository.storeToken(token, user, serverTime);
		user.setToken(token);

		return token;
	}

	@Override
	public User getUserFromToken(String token) {

		Token userToken = tokensRepository.getUserFromToken(token);
		long serverTime = System.currentTimeMillis() - TEN_MINUTES_IN_MILLIS;

		if (userToken != null) {
			if (serverTime > userToken.getCreationTime()) {
				//
				// expired token we will return null
				//
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("expired token [ " + token + "] for user [" + userToken.getUser().getId() + "]");
				}

				return null;
			} else {
				//
				// everythings seems ok, return linked user
				//
				return userToken.getUser();
			}
		} else {
			//
			// although maybe it will be useful to know if somebody is trying
			// tokens, this could fill log files, and maybe make system crash
			// then we leave as FINE
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("invalid token [ " + token + "]");
			}
			return null;
		}
	}

}

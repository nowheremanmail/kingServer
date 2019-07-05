package com.dag.king.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.Token;
import com.dag.king.model.User;
import com.dag.king.repository.TokensRepository;

/**
 * This is another implementation to reduce impact on token expiration
 * validation. We will remove tokens every minute
 * 
 * @author david.galindo
 *
 */
public class TokensRepositoryOtherImpl implements TokensRepository {

	private static Logger logger = Logger.getLogger("TokensRepositoryOtherImpl");

	private static final String SYNC = "SYNC";

	//
	//
	// We will keep a map with all current tokens
	private Map<String, Token> tokens;
	//
	// a list of current tokens in (aprox) creation order
	// we will use to detect expired tokens.
	private Queue<Token> tokensInCreationOrder;

	public TokensRepositoryOtherImpl() {
		tokens = new HashMap<String, Token>(1024*1024);
		tokensInCreationOrder = new ConcurrentLinkedQueue<Token>();
	}

	@Override
	public void clean(long serverTime) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("cleaning, start");
		}
		//
		// we are removing tokens while we are finding expired tokens on list
		int N = 0;
		//int totalTokens = 0;
		while (true) {
			Token userToken = tokensInCreationOrder.peek();
			if (userToken == null) {
				break;
			}
			if (serverTime > userToken.getCreationTime()) {
				//
				// expired token we will remove from list and map
				//
				userToken = tokensInCreationOrder.poll();

				synchronized (SYNC) {
					tokens.remove(userToken.getToken());
					//totalTokens = tokens.size();
				}
				N++;
			} else {
				break;
			}
		}
		if (logger.isLoggable(Level.INFO)) {
			logger.info("cleaning (" + N + /*"/" + totalTokens +*/ "), end");
		}
	}

	@Override
	public void storeToken(String token, User user, long serverTime) {
		Token userToken = new Token(token, user, serverTime);
		synchronized (SYNC) {
			//
			// we already have lock token map then
			// if user already has one, we remove
			if (user.getToken() != null) {
				tokens.remove(user.getToken());
			}

			tokens.put(token, userToken);
		}
		tokensInCreationOrder.add(userToken);

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("token [ " + token + "] added for user [" + user.getId() + "]");
		}

	}

	@Override
	public Token getUserFromToken(String token) {
		return tokens.get(token);
	}

	// @Override
	// public void remove(String token) {
	// synchronized (SYNC) {
	// tokens.remove(token);
	// }
	// }

}

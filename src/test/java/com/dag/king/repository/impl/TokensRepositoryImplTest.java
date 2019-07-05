package com.dag.king.repository.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.dag.king.model.Token;
import com.dag.king.model.User;
import com.dag.king.repository.TokensRepository;
import com.dag.king.repository.impl.TokensRepositoryOtherImpl;

public class TokensRepositoryImplTest {

	//private static final long TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000;

	private TokensRepository tokensRepository;

	@Before
	public void setUp() throws Exception {
		// tokensRepository = new TokensRepositoryImpl();
		// tokensRepository = new TokensRepositoryImpl2();
		tokensRepository = new TokensRepositoryOtherImpl();
	}

	@Test
	public void testStoreToken() {
		User user = new User(1);
		long serverTime = System.currentTimeMillis();

		tokensRepository.storeToken("token1", user, serverTime);
		Token existingUser = tokensRepository.getUserFromToken("token1");

		assertNotNull(existingUser);
	}

	//
	// This test it is only valid depending on implementation
	//
	// @Test
	// public void testStoreNewToken() {
	// User user = new User(1);
	// long serverTime = System.currentTimeMillis();
	//
	// tokensRepository.storeToken("token1", user, serverTime);
	// tokensRepository.storeToken("token2", user, serverTime);
	//
	// User existingUser1 = tokensRepository.getUserFromToken("token1",
	// serverTime);
	// assertNull(existingUser1);
	//
	// User existingUser2 = tokensRepository.getUserFromToken("token2",
	// serverTime);
	// assertNotNull(existingUser2);
	// }

	@Test
	public void testStoreTokens() {
		User user1 = new User(1);
		User user3 = new User(3);
		long serverTime = System.currentTimeMillis();

		tokensRepository.storeToken("token1", user1, serverTime);
		Token existingUser1 = tokensRepository.getUserFromToken("token1");
		assertNotNull(existingUser1);

		tokensRepository.storeToken("token3", user3, serverTime);
		Token existingUser2 = tokensRepository.getUserFromToken("token3");
		assertNotNull(existingUser2);
	}

	@Test
	public void testUnexistingToken() {
		User user = new User(1);
		long serverTime = System.currentTimeMillis();

		tokensRepository.storeToken("token1", user, serverTime);
		Token existingUser = tokensRepository.getUserFromToken("token3");

		assertNull(existingUser);
	}

//	@Test
//	public void testGetTokenBefore10minutes() {
//		User user = new User(1);
//		long serverTime = System.currentTimeMillis();
//
//		tokensRepository.storeToken("token1", user, serverTime);
//		User existingUser = tokensRepository.getUserFromToken("token1", serverTime + TEN_MINUTES_IN_MILLIS - 1);
//
//		assertNotNull(existingUser);
//	}
//
//	@Test
//	public void testGetTokenAfter10minutes() {
//		User user = new User(1);
//		long serverTime = System.currentTimeMillis();
//
//		tokensRepository.storeToken("token1", user, serverTime);
//		User existingUser = tokensRepository.getUserFromToken("token1", serverTime + TEN_MINUTES_IN_MILLIS + 1);
//
//		assertNull(existingUser);
//	}
}

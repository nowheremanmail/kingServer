package com.dag.king.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dag.king.model.User;
import com.dag.king.repository.TokensRepository;
import com.dag.king.repository.impl.TokensRepositoryOtherImpl;
import com.dag.king.service.TokensService;

public class TokensServiceImplTest {
	private TokensRepository repo;

	@Before
	public void setUp() throws Exception {
		repo = new TokensRepositoryOtherImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void generateTokenTest() {
		TokensService tokenService = new TokensServiceImpl(repo);

		String token = tokenService.getTokenForUser(new User(1));

		assertNotNull(token);
	}

	@Test
	public void generateCheckTokenTest() {
		TokensService tokenService = new TokensServiceImpl(repo);

		String token = tokenService.getTokenForUser(new User(1));

		User user = tokenService.getUserFromToken(token);

		assertNotNull(user);
		assertTrue(user.getId() == 1);

	}

	@Test
	public void generateCheckInvalidTokenTest() {
		TokensService tokenService = new TokensServiceImpl(repo);
		
		User user = tokenService.getUserFromToken("invalidtoken");

		assertNull(user);
	}
	// check is token near unique per user
	@Test
	public void mainTest() {
		TokensService tokensService = new TokensServiceImpl(repo);

		User user1 = new User(1);

		Set<String> exists = new HashSet<String>(100000);

		for (int i = 0; i < 100000; i++) {

			String token = tokensService.getTokenForUser(user1);

			assertTrue(exists.add(token));
		}
		
		assertTrue(exists.size()== 100000);
	}

}

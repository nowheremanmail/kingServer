package com.dag.king.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dag.king.model.User;
import com.dag.king.repository.UsersRepository;
import com.dag.king.repository.impl.UsersRepositoryImpl;
import com.dag.king.service.UsersService;
import com.dag.king.service.impl.UsersServiceImpl;

public class UsersServiceImplTest {

	private UsersRepository repo;

	@Before
	public void setUp() throws Exception {
		repo = new UsersRepositoryImpl();
		repo.addUser(1);
		repo.addUser(3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoginExistingUser() {
		UsersService userService = new UsersServiceImpl(repo);

		User user = userService.login(1);

		assertNotNull(user);
		assertTrue(user.getId() == 1);
	}

	@Test
	public void testLoginNewUser() {
		UsersService userService = new UsersServiceImpl(repo);

		User user = userService.login(2);

		assertNotNull(user);
		assertTrue(user.getId() == 2);
	}

}

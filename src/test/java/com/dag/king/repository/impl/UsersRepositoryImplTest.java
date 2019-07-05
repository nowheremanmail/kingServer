package com.dag.king.repository.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dag.king.model.User;
import com.dag.king.repository.impl.UsersRepositoryImpl;

public class UsersRepositoryImplTest {

	@Test
	public void testInit() {
		UsersRepositoryImpl repo = new UsersRepositoryImpl();

		// check that initially nothing exists
		assertFalse(repo.isValid(1));
		assertFalse(repo.isValid(2));
		assertFalse(repo.isValid(3));
	}

	@Test
	public void testCreateUser() {
		UsersRepositoryImpl repo = new UsersRepositoryImpl();

		// check that users can be created
		// user doesn't exists
		assertFalse(repo.isValid(1));

		// create user
		User newUser = repo.addUser(1);
		assertNotNull(newUser);
		// check that user is the same
		assertTrue(newUser.getId() == 1);

		User anotherNewUser = repo.addUser(3);
		assertNotNull(anotherNewUser);
		// check that user is the same
		assertTrue(anotherNewUser.getId() == 3);
	}

	@Test
	public void testCheckUsers() {
		UsersRepositoryImpl repo = new UsersRepositoryImpl();

		User newUser = repo.addUser(1);
		assertNotNull(newUser);

		User anotherNewUser = repo.addUser(3);
		assertNotNull(anotherNewUser);

		// user 1 exists
		assertTrue(repo.isValid(1));
		// user 2 doesn't exists
		assertFalse(repo.isValid(2));
		// user 3 exists
		assertTrue(repo.isValid(3));

	}
}

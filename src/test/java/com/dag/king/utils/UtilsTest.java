package com.dag.king.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dag.king.utils.Utils;

public class UtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsValidIntegerParam() {
		assertFalse(Utils.isValidIntegerParam(null));
		assertFalse(Utils.isValidIntegerParam(""));
		assertTrue(Utils.isValidIntegerParam("1"));
		assertTrue(Utils.isValidIntegerParam("100"));
		assertTrue(Utils.isValidIntegerParam(Integer.MAX_VALUE + ""));
		assertFalse(Utils.isValidIntegerParam(Integer.MAX_VALUE + "0"));
		assertFalse(Utils.isValidIntegerParam("1 1"));
		assertFalse(Utils.isValidIntegerParam("error"));
	}

	@Test
	public void testGetIntegerParam() {
		assertTrue(-1 == Utils.getIntegerParam(null));
		assertTrue(-1 == Utils.getIntegerParam(""));
		assertTrue(1 == Utils.getIntegerParam("1"));
		assertTrue(100 == Utils.getIntegerParam("100"));
		assertTrue(Integer.MAX_VALUE == Utils.getIntegerParam(Integer.MAX_VALUE + ""));
		assertTrue(-1 == Utils.getIntegerParam(Integer.MAX_VALUE + "0"));
		assertTrue(-1 == Utils.getIntegerParam("1 1"));
		assertTrue(-1 == Utils.getIntegerParam("error"));
	}

}

package com.dag.king.repository.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.dag.king.model.Result;
import com.dag.king.repository.ResultsRepository;

public class ResultsRepositoryOtherImplTest {

	private static Logger logger = Logger.getLogger("ResultsRepositoryOtherImplTest");
	
	private ResultsRepository repo;

	@Before
	public void setUp() throws Exception {
		repo = new ResultsRepositoryOtherImpl();
	}
	
	// test that everything seems to work with one
	@Test
	public void addFirstScoreTest() {

		repo.addScore(1, 1, 100);

		Result[] results = repo.getScores(1);

		assertTrue(results[0].getScore() == 100 && results[0].getUserId() == 1);
		assertNull(results[1]);
	}

	// test that everything seems to work with more scores and levels
	@Test
	public void addScoresTest() {

		repo.addScore(1, 1, 100);
		repo.addScore(2, 1, 400);
		repo.addScore(3, 1, 200);
		repo.addScore(4, 1, 300);

		repo.addScore(1, 2, 100);
		repo.addScore(2, 2, 400);

		Result[] results = repo.getScores(1);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

		results = repo.getScores(2);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 100 && results[1].getUserId() == 1);
		assertNull(results[2]);
	}

	// test that add highscores for a user works
	@Test
	public void addScoresUserTwiceByHighScoreTest() {

		repo.addScore(1, 1, 100);
		repo.addScore(2, 1, 400);
		repo.addScore(3, 1, 200);
		repo.addScore(4, 1, 300);

		Result[] results = repo.getScores(1);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

		repo.addScore(2, 1, 450);

		results = repo.getScores(1);

		assertTrue(results[0].getScore() == 450 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

		repo.addScore(1, 1, 150);

		results = repo.getScores(1);

		assertTrue(results[0].getScore() == 450 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 150 && results[3].getUserId() == 1);
		assertNull(results[4]);

		repo.addScore(4, 1, 350);

		results = repo.getScores(1);

		assertTrue(results[0].getScore() == 450 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 350 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 150 && results[3].getUserId() == 1);
		assertNull(results[4]);

	}

	// test that add scores with lower values for existing users works
	@Test
	public void addScoresUserTwiceByLowerScoreTest() {

		repo.addScore(1, 1, 100);
		repo.addScore(2, 1, 400);
		repo.addScore(3, 1, 200);
		repo.addScore(4, 1, 300);

		Result[] results = repo.getScores(1);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

		repo.addScore(2, 1, 390);

		results = repo.getScores(1);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

		repo.addScore(1, 1, 90);

		results = repo.getScores(1);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

		repo.addScore(4, 1, 290);

		results = repo.getScores(1);

		assertTrue(results[0].getScore() == 400 && results[0].getUserId() == 2);
		assertTrue(results[1].getScore() == 300 && results[1].getUserId() == 4);
		assertTrue(results[2].getScore() == 200 && results[2].getUserId() == 3);
		assertTrue(results[3].getScore() == 100 && results[3].getUserId() == 1);
		assertNull(results[4]);

	}

	// test that adding a lot of scores, we have maximum 15 scores and no repeat user
	@Test
	public void addMultipleScoreTest() {

		Random rnd = new Random(11231212);

		Result[] results = null;

		long t = System.currentTimeMillis();

		int maxScore = 0;
		
		for (int i = 0; i < 1000000; i++) {
			int someScore = rnd.nextInt(100000);
			
			if (someScore > maxScore) maxScore = someScore;
			
			repo.addScore(rnd.nextInt(5000), 1, someScore);
			
			results = repo.getScores(1);					
		}
		logger.info("PERFORMANCE2 " + (System.currentTimeMillis() -t));
		
		// it should exists one
		assertNotNull(results[0]);
		// max score should be the highest
		assertTrue(results[0].getScore() == maxScore);

		// to detect no repeated users
		Set<Integer> exists = new HashSet<Integer>();

		for (int i = 0; i < 15; i++) {
			if (results[i] != null) {
				assertTrue(exists.add(results[i].getUserId()));
			}
			if (i < 14 && results[i] != null && results[i + 1] != null) {
				assertTrue(results[i].getScore() >= results[i + 1].getScore());
			}
		}

	}
	
	
	@Test
	public void addFullTest() {

		for (int i = 0; i < 15; i++) {
			repo.addScore(i, 1, 100+i);
		}
		
		Result[] results = repo.getScores(1);

		assertTrue(results[0].getScore() == 114 && results[0].getUserId() == 14);
		
		// Add the best -> first on list
		repo.addScore(14, 1, 2000);
		results = repo.getScores(1);
		assertTrue(results[0].getScore() == 2000 && results[0].getUserId() == 14);
		
		// Add the worst -> no on list
		repo.addScore(0, 1, 1);
		results = repo.getScores(1);
		assertTrue(results[14].getScore() == 100 && results[14].getUserId() == 0);

		//  Add the best -> first on list
		repo.addScore(0, 1,3000);
		results = repo.getScores(1);
		assertTrue(results[0].getScore() == 3000 && results[0].getUserId() == 0);
		assertTrue(results[14].getScore() == 101 && results[14].getUserId() == 1);

		//  Add repeat value
		repo.addScore(1, 1,3000);
		results = repo.getScores(1);
		assertTrue(results[0].getScore() == 3000 && results[0].getUserId() == 0);
		assertTrue(results[1].getScore() == 3000 && results[1].getUserId() == 1);
		assertTrue(results[14].getScore() == 102 && results[14].getUserId() == 2);

	}

}

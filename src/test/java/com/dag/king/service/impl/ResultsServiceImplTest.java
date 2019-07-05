package com.dag.king.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dag.king.model.User;
import com.dag.king.repository.impl.ResultsRepositoryOtherImpl;
import com.dag.king.service.ResultsService;

public class ResultsServiceImplTest {

	@Test
	public void mainTest() {
		ResultsService resultsService = new ResultsServiceImpl(new ResultsRepositoryOtherImpl());
		
		User user1 = new User(1);
		User user2 = new User(2);
		User user3 = new User(3);
		User user4 = new User(4);
		
		// Initial empty list
		assertTrue (resultsService.getScoreList(0).length() == 0);
		
		// add user 1 with score 100
		resultsService.store(0, user1, 100);		
		assertTrue ("1=100".equals(resultsService.getScoreList(0)));

		// add user 2 with score 200
		resultsService.store(0, user2, 200);		
		assertTrue ("2=200,1=100".equals(resultsService.getScoreList(0)));	
		
		// add user 3 and 4 with score 300, and 400
		resultsService.store(0, user3, 300);		
		resultsService.store(0, user4, 400);		
		assertTrue ("4=400,3=300,2=200,1=100".equals(resultsService.getScoreList(0)));	
		
	}

}

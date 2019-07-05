package com.dag.king.service;

import com.dag.king.model.User;

public interface ResultsService {

	/**
	 * update score list for given level with given user's score
	 * 
	 * @param levelId
	 * @param user
	 * @param score
	 */
	void store(int levelId, User user, int score);
	
	/**
	 * returns the current score list for given level in CSV format
	 * 
	 * @param levelId
	 * @return
	 */
	String getScoreList(int levelId);
}

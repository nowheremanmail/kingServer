package com.dag.king.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.Result;
import com.dag.king.model.User;
import com.dag.king.repository.ResultsRepository;
import com.dag.king.service.ResultsService;

public class ResultsServiceImpl implements ResultsService {
	private static Logger logger = Logger.getLogger("ResultsServiceImpl");

	private ResultsRepository resultsRepository;

	public ResultsServiceImpl(ResultsRepository resultsRepositoryImpl) {
		resultsRepository = resultsRepositoryImpl;
	}

	@Override
	public void store(int levelId, User user, int score) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("store score [ " + score + "] for user [" + user.getId() + "] and level " + levelId);
		}
	
		resultsRepository.addScore(user.getId(), levelId, score);
	}

	@Override
	public String getScoreList(int levelId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("get score list for level " + levelId);
		}
		
		StringBuilder tmp = new StringBuilder();

		Result[] results = resultsRepository.getScores(levelId);

		for (int i = 0, N = results != null ? results.length : 0; i < N && results[i] != null; i++) {
			if (i > 0)
				tmp.append(",");
			tmp.append(results[i].getUserId()).append("=").append(results[i].getScore());
		}

		return tmp.toString();
	}

}

package com.dag.king.repository.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.Result;
import com.dag.king.model.Results;
import com.dag.king.repository.ResultsRepository;

public class ResultsRepositoryImpl implements ResultsRepository {

	private static Logger logger = Logger.getLogger("ResultsRepositoryImpl");

	private static final String SYNC = "SYNC";

	private Map<Integer, Results> results;

	public ResultsRepositoryImpl() {
		results = new HashMap<Integer, Results>(2048);
	}

	@Override
	public void addScore(int userId, int level, int score) {
		Results result = results.get(level);

		if (result == null) {
			synchronized (SYNC) {
				result = results.get(level);

				if (result == null) {
					result = new Results(level);
					results.put(level, result);

					if (logger.isLoggable(Level.INFO)) {
						logger.info("score list for level [" + level + "] created");
					}

					result.getByUser().put(userId, 0);
					result.getResults()[0] = new Result(userId, score);
					result.setNumScores(1);
					return;
				}
			}
		}

		// ignore scores that are not going to be on list because are lower than
		// lowest
		if (result.getNumScores() == 15 && result.getResults()[14].getScore() >= score) {
			return;
		}

		// only one thread can update this level
		synchronized (result) {
			//
			// we will work in a temporary array to avoid concurrent access
			// between this update and reads
			//

			Result[] resultsTmp = Arrays.copyOf(result.getResults(), 15);
			Map<Integer, Integer> byUser = result.getByUser();

			int N = result.getNumScores();

			int start;
			//
			Integer pos = byUser.get(userId);
			if (pos != null) {
				// user is on list,
				// we will add new score only if it is best than previous
				// one
				if (score <= resultsTmp[pos].getScore()) {
					// not better ignore
					return;
				}

				start = pos;
			} else {
				// user is not in the list
				start = N;

				// start with lowest value on list
				if (score <= resultsTmp[start - 1].getScore()) {
					// new score is not better that worst on list
					return;
				}

			}

			// move score to up on list, until its position
			while (start > 0 && score > resultsTmp[start - 1].getScore()) {
				if (start < 15) {
					// update new position for user
					resultsTmp[start] = resultsTmp[start - 1];
					byUser.put(resultsTmp[start].getUserId(), start);
				} else {
					// this is last user, we can remove
					byUser.remove(resultsTmp[start - 1].getUserId());
				}

				start--;
			}

			if (start >= 0) {
				resultsTmp[start] = new Result(userId, score);
				byUser.put(userId, start);
			}

			// update num of scores on list, only if user didn't exist 
			if (pos == null) {
				result.setNumScores(N < 15 ? N + 1 : 15);
			}
			// set new list
			result.setResults(resultsTmp);
		}
	}

	@Override
	public Result[] getScores(int level) {
		Results result = results.get(level);
		if (result == null)
			return null;
		return result.getResults();
	}

}

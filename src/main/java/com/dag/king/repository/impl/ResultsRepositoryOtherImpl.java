package com.dag.king.repository.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.Result;
import com.dag.king.model.Results;
import com.dag.king.repository.ResultsRepository;

public class ResultsRepositoryOtherImpl implements ResultsRepository {

	private static Logger logger = Logger.getLogger("ResultsRepositoryImpl");

	private static final String SYNC = "SYNC";

	private Map<Integer, Results> results;

	public ResultsRepositoryOtherImpl() {
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

					result.getResults()[0] = new Result(userId, score);

					if (logger.isLoggable(Level.INFO)) {
						logger.info("score list for level [" + level + "] created");
					}

					return;
				}
			}
		}

		// only one thread can update this level
		synchronized (result) {
			//
			// we will work in a temporary array to avoid concurrent access
			// between this update and reads
			//

			Result[] resultsTmp = Arrays.copyOf(result.getResults(), 15);

			int position = -1, positionByUser = -1;
			int i = 0;

			for (; i < 15 && resultsTmp[i] != null; i++) {
				if (resultsTmp[i].getUserId() == userId) {
					positionByUser = i;
				}
				if (position == -1 && resultsTmp[i].getScore() < score) {
					position = i;
				}
			}

			if (i == 0) {
				resultsTmp[0] = new Result(userId, score);
			} else {
				if (positionByUser != -1) {
					// user is on list,
					// we will add new score only if it is best than previous
					// one
					if (score <= resultsTmp[positionByUser].getScore()) {
						// not better ignore
						return;
					}

					if (position != positionByUser) {
						System.arraycopy(resultsTmp, position, resultsTmp, position + 1, positionByUser - position);
						resultsTmp[position] = new Result(userId, score);
					} else {
						resultsTmp[positionByUser].setScore(score);
					}

				} else {
					if (position == -1) {
						return;
					}
					// user is not in the list
					// move all position down and add new one
					if (position < 14) {
						System.arraycopy(resultsTmp, position, resultsTmp, position + 1, 14 - position);
					}
					resultsTmp[position] = new Result(userId, score);
				}

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

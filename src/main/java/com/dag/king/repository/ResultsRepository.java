package com.dag.king.repository;

import com.dag.king.model.Result;

/**
 * This will responsible to store scores list for each level
 * 
 * @author david.galindo
 *
 */
public interface ResultsRepository {
	/**
	 * Update score list for given level
	 * Assumptions:
	 * - one list per level with maximum 15 users
	 * - User could only appears once, if user exists it will appears with higher score 
	 * - we allow repeat scores (for differents users)
	 * 
	 * @param userId
	 * @param level
	 * @param score
	 */
	public void addScore(int userId, int level, int score);

	/**
	 * Get current score list for given level
	 * 
	 * @param level
	 * @return
	 */
	public Result[] getScores(int level);
}

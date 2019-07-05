package com.dag.king.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Persistence for score list for one level
 * 
 * @author david
 *
 */
public class Results {

	private volatile Result[] results;

	private Map<Integer, Integer> byUser;
	private int level;

	private int numScores = 0;

	public Results(int level) {
		this.level = level;

		byUser = new HashMap<Integer, Integer>(16);

		// we will keep only 15 values already initialized
		results = new Result[15];

		for (int i = 0, N = results.length; i < N; i++) {
			results[i] = null; // new Result();
		}
		numScores = 0;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public Map<Integer, Integer> getByUser() {
		return byUser;
	}

	public void setResults(Result[] results) {
		this.results = results;
	}

	public Result[] getResults() {
		return results;
	}

	public int getNumScores() {
		return numScores;
	}

	public void setNumScores(int numScores) {
		this.numScores = numScores;
	}
}

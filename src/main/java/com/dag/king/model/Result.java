package com.dag.king.model;

/**
 * persistence one user score
 * 
 * @author david
 *
 */
public class Result {
	private int userId = -1;
	private int score = -1;

	public Result(int userId, int score) {
		this.userId = userId;
		this.score = score;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("[").append(userId).append(" - ").append(score).append("]");

		return b.toString();
	}
}

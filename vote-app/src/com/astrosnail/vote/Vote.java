package com.astrosnail.vote;

import java.util.Date;

/* Class that represents a vote in the competition.
 * Corresponds to VOTE entity/table */
public class Vote implements Comparable<Vote> {
	
	private User user;
	private Candidate candidate;
	private Date createdAt;

	public Vote(User user, Candidate candidate, Date createdAt) {
		this.user = user;
		this.candidate = candidate;
		this.createdAt = createdAt;		
	}
	
	public Vote(User user, Candidate candidate) {
		this(user, candidate, new Date());		
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "User: " + user.getId() + 
				" voted for: " + candidate.getId() + " " + candidate.getName() +
				" at " + createdAt;
	}

	@Override
	public int compareTo(Vote o) {
		// TODO Auto-generated method stub
		return 0;
	}
		
}

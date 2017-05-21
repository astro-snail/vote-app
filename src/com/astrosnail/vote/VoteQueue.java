package com.astrosnail.vote;

public interface VoteQueue {
	
	public void addVote(Vote vote);
	
	public Vote getNextVote();

}

package com.astrosnail.vote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteProcessor {

	private static final int MAX_VOTE_COUNT = 3;
	
	private VoteQueue queue;
	private Map<Candidate, Candidate> voteResults = new HashMap<>();
	
	public VoteProcessor(List<Candidate> candidates, VoteQueue queue) {
		this.queue = queue;
		
		for (Candidate candidate : candidates) {
			candidate.setTotalVotes(0L);
			voteResults.put(candidate, candidate);
		}
	}
	
	private Vote getNextVote() {
		
		Vote nextVote = null;
		
		synchronized (queue) {
			nextVote = queue.getNextVote();
		}
		return nextVote;
	}
	
	private void processVote(Vote nextVote) {

		User user = User.getUser(nextVote.getUser().getId());
		
		if (user.getVoteCount() < MAX_VOTE_COUNT) {
			Candidate candidate = voteResults.get(nextVote.getCandidate());
			candidate.addVote();
			user.addVoteCount();
		}	
	}
	
	public void countVotes() {
		
		Vote nextVote = null;
		
		while ((nextVote = getNextVote()) != null) {
			processVote(nextVote);
		}
	}
	
	public Map<Candidate, Candidate> getVoteResults() {
		return voteResults;
	}
	
}

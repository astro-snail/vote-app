package com.astrosnail.vote;

import java.util.List;
import java.util.Random;

public class VoteGenerator implements Runnable{

	private final long numberOfVotes;
	private final List<Candidate> candidates;
	private VoteQueue queue;
	private Random random = new Random();
	
	public VoteGenerator(List<Candidate> candidates, VoteQueue queue, long numberOfVotes) {
		this.candidates = candidates;
		this.queue = queue;
		this.numberOfVotes = numberOfVotes;
	}
	
	public Vote generateRandomVote() {
		
		long userId = (long)(random.nextDouble() * numberOfVotes / 10);
		short candidateIndex = (short)(random.nextInt(candidates.size()));
		
		User user = User.getUser(userId);
		Candidate candidate = candidates.get(candidateIndex);
		
		return new Vote(user, candidate);
	}

	@Override
	public void run() {
		// Run vote generator
		for (long i = 0; i < numberOfVotes; i++) {
			// Simulate some delay
			try {
				Thread.sleep(500);
			}	
			catch (InterruptedException e) {}
					
			Vote vote = generateRandomVote();

			synchronized (queue) {
				queue.addVote(vote);
			}
		}
	}

}

package com.astrosnail.vote;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class VoteGenerator {
		
	private List<Candidate> candidates;

	public static void main(String[] args) {
		
		int n = 100;
		System.out.println(n + " randomly generated votes:");
		
		// Run vote generator
		VoteGenerator voteGenerator = new VoteGenerator();		
		VoteQueue voteQueue = new VoteQueue(voteGenerator.getCandidates());
		
		for (int i = 0; i < n; i++) {
			
			Vote vote = voteGenerator.generateRandomVote();
			voteQueue.addVote(vote);
			
			System.out.println(vote);
		}
		
		System.out.println();
		
		for (Candidate candidate : voteQueue.getQueues().keySet()) {
			
			Queue<Vote> queue = voteQueue.getQueue(candidate);
			
			System.out.println("Queue for candidate: " + candidate + ", votes: " + queue.size());
			
			while (queue.peek() != null) {
			
				Vote vote = queue.poll();
				System.out.println(vote);
			}	
			
			System.out.println();
		}
	}
	
	public VoteGenerator() {
		this.candidates = createCandidates();
	}
	
	public List<Candidate> getCandidates() {
		return candidates;
	}
	
	public List<Candidate> createCandidates() {
		
		List<Candidate> candidates = new ArrayList<>();
		
		candidates.add(new Candidate((short)1, "Mickey Mouse"));
		candidates.add(new Candidate((short)2, "Snow White"));
		candidates.add(new Candidate((short)3, "Cinderella"));
		candidates.add(new Candidate((short)4, "Puss in Boots"));
		candidates.add(new Candidate((short)5, "Peppa Pig"));
		
		return candidates;
		
	}
	
	public Vote generateRandomVote() {
		
		Random random = new Random();
		
		User user = new User(random.nextLong());
		Candidate candidate = candidates.get((short)random.nextInt(candidates.size()));
		
		return new Vote(user, candidate);
	}
	
}

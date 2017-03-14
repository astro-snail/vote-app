package com.astrosnail.vote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class VoteQueue {
	
	private Map<Candidate, Queue<Vote>> queues;
	
	public VoteQueue(List<Candidate> candidates) {
		
		this.queues = new HashMap<Candidate, Queue<Vote>>();
		
		for (Candidate candidate : candidates) {
			queues.put(candidate, new PriorityQueue<>());
		}
	}
	
	public Map<Candidate, Queue<Vote>> getQueues() {
		return queues;
	}
	
	public void addVote(Vote vote) {
		Queue<Vote> queue = getQueue(vote);
		queue.add(vote);
	}

	public Queue<Vote> getQueue(Vote vote) {
		return queues.get(vote.getCandidate());
	}
	
	public Queue<Vote> getQueue(Candidate candidate) {
		return queues.get(candidate);
	}

}

package com.astrosnail.vote;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class MultiQueue implements VoteQueue {
	
	private Map<Candidate, Queue<Vote>> queues;
	
	public MultiQueue(List<Candidate> candidates) {
		
		this.queues = new HashMap<Candidate, Queue<Vote>>();
		
		for (Candidate candidate : candidates) {
			queues.put(candidate, new PriorityQueue<>());
		}
	}
	
	public Map<Candidate, Queue<Vote>> getQueues() {
		return queues;
	}
	
	public Queue<Vote> getQueue(Vote vote) {
		return queues.get(vote.getCandidate());
	}
	
	public Queue<Vote> getQueue(Candidate candidate) {
		return queues.get(candidate);
	}
	
	@Override
	public void addVote(Vote vote) {
		Queue<Vote> queue = getQueue(vote);
		if (queue != null) {
			queue.add(vote);	
		}
	}
	
	@Override
	public Vote getNextVote() {
		
		Date createdAt = new Date(); 
		Vote nextVote = null;

		for (Queue<Vote> queue : queues.values()) {
			
			Vote vote = null;
			
			if ((vote = queue.peek()) != null) {
				if (vote.getCreatedAt().before(createdAt)) {
					createdAt = vote.getCreatedAt();
					nextVote = vote;
				}
			}
		}
		if (nextVote != null) {
			getQueue(nextVote).poll();
		}
		return nextVote;
	}

}

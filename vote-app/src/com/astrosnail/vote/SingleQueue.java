package com.astrosnail.vote;

import java.util.PriorityQueue;
import java.util.Queue;

public class SingleQueue implements VoteQueue {
	
	private Queue<Vote> queue;
	
	public SingleQueue() {
		this.queue = new PriorityQueue<Vote>();
	}

	@Override
	public void addVote(Vote vote) {
		queue.add(vote);
	}

	@Override
	public Vote getNextVote() {
		return queue.poll();
	}

}

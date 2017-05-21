package com.astrosnail.vote;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VoteApplication {
	
	public static void outputResults(VoteProcessor voteProcessor) {
		System.out.println(new Date());

		voteProcessor.countVotes();
		for (Candidate candidate : voteProcessor.getVoteResults().values()) {
			System.out.println(candidate);
		}
		
		System.out.println();
	}

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		int queueType = scanner.nextInt();
		long numberOfVotes = scanner.nextLong(); 
		scanner.close();

		List<Candidate> candidates = Candidate.createCandidates();
        
		VoteQueue queue;
		switch (queueType) {
		case 1:
			queue = new SingleQueue();
			break;
		case 2:
			queue = new MultiQueue(candidates);
			break;
		default:
			return;
		}
		
		Thread voteGenerator = new Thread(new VoteGenerator(candidates, queue, numberOfVotes));
		VoteProcessor voteProcessor = new VoteProcessor(candidates, queue);
		
		voteGenerator.start();

		try {
			while (voteGenerator.isAlive()) {
				outputResults(voteProcessor);
				Thread.sleep(1000);
			}
			voteGenerator.join();
		} catch (InterruptedException e) {}
		
		System.out.println("Final result:");
        outputResults(voteProcessor);		
	}

}

package com.astrosnail.vote;

/* Class that represents a candidate in the competition.
 * Corresponds to the CANDIDATES entity/table */
public class Candidate implements Comparable<Candidate> {
	
	private short id;
	private String name;
	private long totalVotes;
	
	public Candidate(short id) {
		this(id, null);
	}
	
	public Candidate(short id, String name) {
		this(id, name, 0);
	}
	
	public Candidate(short id, String name, long totalVotes) {
		this.id = id;
		this.name = name;
		this.totalVotes = totalVotes;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(long totalVotes) {
		this.totalVotes = totalVotes;
	}

	/* Supports sorting of the collection first by number of votes descending
	 * and then by the name in alphabetical order
	 */
	@Override
	public int compareTo(Candidate other) {
		if (Long.compare(totalVotes, other.totalVotes) == 0) {
			return name.compareTo(other.name);
		} else {
			return Long.compare(other.totalVotes, totalVotes);
		}
	}

}

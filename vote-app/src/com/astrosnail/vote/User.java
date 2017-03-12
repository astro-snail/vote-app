package com.astrosnail.vote;

public class User {
	
	private long id;
	private short voteCount;
	
	public User(long id) {
		this(id, (short)0);
	}
	
	public User(long id, short voteCount) {
		this.id = id;
		this.voteCount = voteCount;
	}
	
	public long getId() {
		return id;
	}

	public short getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(short voteCount) {
		this.voteCount = voteCount;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
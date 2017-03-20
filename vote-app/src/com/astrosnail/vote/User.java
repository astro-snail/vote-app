package com.astrosnail.vote;

import java.util.HashMap;
import java.util.Map;

/* Class that represents a user taking part in the vote.
 * Corresponds to USERS entity/table */
public class User {

	private static Map<Long, User> users = new HashMap<>();
	
	private long id;
	private short voteCount;
	
	public User(long id) {
		this(id, (short)0);
	}
	
	public User(long id, short voteCount) {
		this.id = id;
		this.voteCount = voteCount;
	}
	
	public static User getUser(long id) {
		
		User user = null;
		
		if (!users.containsKey(id)) {
			user = new User(id);
			users.put(id, user);
		} else {
			user = users.get(id);
		}	
		return user;
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
	
	public void addVoteCount() {
		this.voteCount++;
	}
	
}

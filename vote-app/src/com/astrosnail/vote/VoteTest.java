package com.astrosnail.vote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class VoteTest {
	
	public static void main(String[] args) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			
			String dbURL = "jdbc:derby:" + System.getProperty("user.home") + "/VoteDB";
			Properties properties = new Properties();
			properties.put("create", "true");
			properties.put("user", "root");
			properties.put("password", "root");
			 
			Connection conn = DriverManager.getConnection(dbURL, properties);
			
			/*VoteDbUtil voteDbUtil = new VoteDbUtil(dataSource);
			voteDbUtil.setUpDatabase();
			
			List<Candidate> candidate = voteDbUtil.getCandidates();
			
			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < candidate.size(); j++) {
					Vote vote = new Vote(new User(i), new Candidate((short)(j+1)));
					voteDbUtil.addVote(vote);
				}	
			}
			
			Map<Short, Long> voteResult = voteDbUtil.getVoteResult();
			
			for (Short key : voteResult.keySet()) {
				System.out.println(key + " " + voteResult.get(key));
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

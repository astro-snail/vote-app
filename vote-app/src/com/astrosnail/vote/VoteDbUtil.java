package com.astrosnail.vote;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteDbUtil {
	
	private DataSource dataSource;
	
	public VoteDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setUpDatabase() throws SQLException {
		createTables();
		addCandidates();
	}
	
	public void createTables() throws SQLException {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			statement = connection.createStatement();

			String sql = "CREATE TABLE candidates ( " + 
						 "candidate_id SMALLINT NOT NULL, " +
						 "candidate_name VARCHAR(50), " +
						 "PRIMARY KEY(candidate_id))";
			statement.addBatch(sql);
			
			sql = "CREATE TABLE users ( " + 
				  "user_id BIGINT NOT NULL, " +
				  "vote_count SMALLINT NOT NULL, " +
				  "PRIMARY KEY(user_id))";
		    statement.addBatch(sql);
			
			sql = "CREATE TABLE votes ( " +
				  "id BIGINT GENERATED ALWAYS AS IDENTITY, " +
				  "user_id BIGINT NOT NULL, " +
				  "candidate_id SMALLINT NOT NULL, " +
				  "created_at TIMESTAMP NOT NULL, " +
				  "PRIMARY KEY (id))";
			statement.addBatch(sql);
			
			sql = "CREATE INDEX idx_candidate_id ON votes(candidate_id)";
			statement.addBatch(sql);
			
			statement.executeBatch();
			connection.commit();

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addCandidates() throws SQLException {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			statement = connection.createStatement();
						
			String sql = "INSERT INTO candidates (candidate_id, candidate_name) VALUES (1, 'Mickey Mouse')";
			statement.addBatch(sql);
			
			sql = "INSERT INTO candidates (candidate_id, candidate_name) VALUES (2, 'Peppa Pig')";
			statement.addBatch(sql);
			
			sql = "INSERT INTO candidates (candidate_id, candidate_name) VALUES (3, 'Snow White')";
			statement.addBatch(sql);
			
			sql = "INSERT INTO candidates (candidate_id, candidate_name) VALUES (4, 'Daffy Duck')";
			statement.addBatch(sql);
			
			sql = "INSERT INTO candidates (candidate_id, candidate_name) VALUES (5, 'Cinderella')";
			statement.addBatch(sql);
			
			sql = "INSERT INTO candidates (candidate_id, candidate_name) VALUES (6, 'Puss in Boots')";
			statement.addBatch(sql);
						
			statement.executeBatch();
			connection.commit();

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Candidate> getCandidates() throws SQLException {
		
		List<Candidate> candidates = new ArrayList<>();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "SELECT candidate_id, candidate_name FROM candidates ORDER BY candidate_name";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			
			while (resultSet.next()) {
				short id = resultSet.getShort(1);
				String name = resultSet.getString(2);
				candidates.add(new Candidate(id, name));
			}
			
			return candidates;

		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<Short, Long> getVoteResult() throws SQLException {
		
		Map<Short, Long> voteResult = new HashMap<>();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "SELECT candidate_id, COUNT(id) FROM votes GROUP BY candidate_id";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			
			while (resultSet.next()) {
				short id = resultSet.getShort(1);
				long totalVotes = resultSet.getLong(2);
				voteResult.put(id, totalVotes);
			}
			
			return voteResult;

		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public User getUser(long id) throws SQLException {

		User user = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "SELECT vote_count FROM users WHERE user_id = ?";
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = new User(id, resultSet.getShort(1));
			} else {
				user = new User(id);
			}
			return user;
			
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateUser(User user) throws SQLException {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		short voteCount = user.getVoteCount();
		
		try {
			connection = dataSource.getConnection();
			String sql = "UPDATE users SET vote_count = ? WHERE user_id = ?";
			statement = connection.prepareStatement(sql);
			statement.setShort(1, ++voteCount);
			statement.setLong(2, user.getId());
			statement.execute();

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addUser(User user) throws SQLException {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		short voteCount = user.getVoteCount();
		
		try {
			connection = dataSource.getConnection();
			String sql = "INSERT INTO users(user_id, vote_count) VALUES(?, ?)";
			statement = connection.prepareStatement(sql);
			statement.setLong(1, user.getId());
			statement.setShort(2, ++voteCount);
			statement.execute();

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void addVote(Vote vote) throws SQLException {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = dataSource.getConnection();
			
			String sql = "INSERT INTO votes (user_id, candidate_id, created_at) VALUES (?, ?, ?)";
			
			statement = connection.prepareStatement(sql);
			statement.setLong(1, vote.getUser().getId());
			statement.setShort(2, vote.getCandidate().getId());
			statement.setTimestamp(3, new Timestamp(vote.getCreatedAt().getTime()));
			
			statement.execute();
			
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}

package com.astrosnail.vote;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class VoteDbUtilTest {
	
	VoteDbUtil voteDbUtil;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        try {
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");            
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:/comp");
            ic.createSubcontext("java:/comp/env");
            ic.createSubcontext("java:/comp/env/jdbc");
           
            // Construct DataSource
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl("jdbc:derby:Databases/VoteDB;create=true");
            ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
            ds.setUsername("root");
            ds.setPassword("root");
            
            ic.bind("java:/comp/env/jdbc/vote_db", ds);
            
        } catch (NamingException ex) {
            Logger.getLogger(VoteDbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	@Before
	public void setUpBefore() {
		try { 
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
		
			DataSource dataSource = (DataSource)webContext.lookup("jdbc/vote_db");
			voteDbUtil = new VoteDbUtil(dataSource);
			
			try {
				voteDbUtil.setUpDatabase();
			} catch (SQLException e) {}
			
		} catch (NamingException ex) {
            Logger.getLogger(VoteDbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }	
	}

	@Test
	public void testCreateTables() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddCandidates() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCandidates() {
		try {
			assertTrue(voteDbUtil.getCandidates().size() == 6);
		} catch (SQLException e) {	
			fail(e.getMessage());
		}	
	}

	@Test
	public void testGetVoteResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUser() {
		try {
			try {
				voteDbUtil.addUser(new User(1));
			} catch (SQLException e) {}	
			assertTrue(voteDbUtil.getUser(1).getId() == 1);
		} catch (SQLException e) {	
			fail(e.getMessage());
		}	
	}

	@Test
	public void testUpdateUser() {
		try {
			User user = voteDbUtil.getUser(1);
			short voteCount = user.getVoteCount();
			voteDbUtil.updateUser(user);
			assertTrue(voteDbUtil.getUser(1).getVoteCount() == ++voteCount);
		} catch (SQLException e) {	
			fail(e.getMessage());
		}	
	}

	@Test
	public void testAddUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddVote() {
		try {
			Map<Short, Long> voteResult = voteDbUtil.getVoteResult();
			
			short candidateId = 1;
			long userId = 1;
			
			long count;
			
			if (!voteResult.containsKey(candidateId)) {
				count = 0;
			} else {
				count = voteResult.get(candidateId);
			}
			voteDbUtil.addVote(new Vote(new User(userId), new Candidate(candidateId)));
			
			voteResult = voteDbUtil.getVoteResult();
			assertEquals(++count, (long)voteResult.get(candidateId));
			
		} catch (SQLException e) {	
			fail(e.getMessage());
		}
		
	}

}

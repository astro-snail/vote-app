package com.astrosnail.vote;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/vote_db")
	private DataSource dataSource;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		List<Candidate> candidates = new ArrayList<>();
		
		boolean isError = false;
		
		try {
			System.setProperty("derby.system.home", getServletContext().getRealPath(File.separator));
			
			/* Test database connection */
			VoteDbUtil voteDbUtil = new VoteDbUtil(dataSource);
			try {
				candidates = voteDbUtil.getCandidates();
			} catch (SQLException e) {
				voteDbUtil.setUpDatabase();
				candidates = voteDbUtil.getCandidates();
			}
			
			if (candidates.size() != 6) {
				isError = true;
				out.println("Number of candidates is " + candidates.size());
		    }
			
			/* Test votes are being added */
			Map<Short, Long> voteResult = voteDbUtil.getVoteResult();
			
			long id = 999999999L;
			User user = voteDbUtil.getUser(id);
			Candidate candidate = candidates.get(0);
	
			long totalVotes = 0;
			if (voteResult.containsKey(candidate.getId())) {
				totalVotes = voteResult.get(candidate.getId());
			}
			
			/* Add vote */
			voteDbUtil.addVote(new Vote(user, candidate));
						
			/* Add vote */
			voteDbUtil.addVote(new Vote(user, candidate));
				
			voteResult = voteDbUtil.getVoteResult();
			if (voteResult.get(candidate.getId()) - totalVotes != 2) {
				isError = true;
				out.println("Number of votes added is wrong");
			}
			
			if (!isError) {
				out.println("No errors");
			}
										
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

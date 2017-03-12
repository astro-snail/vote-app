package com.astrosnail.vote;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			System.setProperty("derby.system.home", getServletContext().getRealPath(File.separator));
			
			/*connection = dataSource.getConnection();
			String sql = "select * from candidates";
			statement = connection.createStatement();
			
			result = statement.executeQuery(sql);
			
			while (result.next()) {
				String candidateName = result.getString("candidate_name");
				out.println(candidateName);
			}*/
			
			VoteDbUtil voteDbUtil = new VoteDbUtil(dataSource);

			List<Candidate> candidates = voteDbUtil.getCandidates();
			
			/*for (int i = 0; i < 1000000; i++) {
				for (int j = 0; j < candidates.size(); j++) {
					Vote vote = new Vote(new User(i), new Candidate((short)(j+1)));
					voteDbUtil.addVote(vote);
				}	
			}*/
			
			Map<Short, Long> voteResult = voteDbUtil.getVoteResult();
			
			for (Short key : voteResult.keySet()) {
				out.println(key + " " + voteResult.get(key));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

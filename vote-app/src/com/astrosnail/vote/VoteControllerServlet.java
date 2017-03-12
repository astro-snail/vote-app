package com.astrosnail.vote;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

/**
 * Servlet implementation class VoteControllerServlet
 */
@WebServlet("/")
public class VoteControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final short MAX_VOTES = 3;
	public static final short MIN_LENGTH = 7;
	public static final short MAX_LENGTH = 10; 
	
	@Resource(name = "jdbc/vote_db")
	private DataSource dataSource;

    private VoteDbUtil voteDbUtil;
    private List<Candidate> candidates = new ArrayList<>();
       
    public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		System.setProperty("derby.system.home", getServletContext().getRealPath(File.separator));
		try {
			voteDbUtil = new VoteDbUtil(dataSource);
			try {
				candidates = voteDbUtil.getCandidates();
			} catch (SQLException e) {};	
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (candidates.isEmpty()) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/setup.jsp");
			dispatcher.forward(request, response);
		} else {		
			String command = request.getParameter("submit");
		
			if (command == null) {
				command = "getCandidates";
			}
			switch (command) {
				case "getCandidates":
					getCandidates(request, response);
					break;
				case "getResults":
					getResults(request, response);	
					break;
				default:
					getCandidates(request, response);
			}
		}	
	}
	
	private void getCandidates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("candidates", candidates);
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vote.jsp");
		dispatcher.forward(request, response);
	}
	
	private void setUpDatabase(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			voteDbUtil.setUpDatabase();
			candidates = voteDbUtil.getCandidates();
		} catch (Exception e) {
			throw new ServletException();
		}
		response.sendRedirect(request.getContextPath() + request.getServletPath());
	}
	
	private boolean isInputValid(HttpServletRequest request, HttpServletResponse response) {
		
		String phoneNumber = request.getParameter("phoneNumber");
		String vote = request.getParameter("candidate");
		
		if (vote == null || "".equals(vote)) {
			String message = "Please select your candidate!";
			request.getSession().setAttribute("errorMessage", message);
			return false;
		}
		
		if (phoneNumber == null || "".equals(phoneNumber)) {
			String message = "Please enter your phone number!";
			request.getSession().setAttribute("errorMessage", message);
			return false;
		}
		
		try {
			Long.parseLong(phoneNumber);
			if (phoneNumber.length() < MIN_LENGTH || phoneNumber.length() > MAX_LENGTH) {
				String message = "Phone number must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " digits long. Please try again!";
				request.getSession().setAttribute("errorMessage", message);
				return false;
			} else {
				return true;
			}
		} catch (NumberFormatException e) {
			String message = "Phone number should only contain digits. Please try again!";
			request.getSession().setAttribute("errorMessage", message);
			return false;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String command = request.getParameter("submit");
		
		switch (command) {
			case "addVote": 
				addVote(request, response);		
				break;
			case "setUp":
				setUpDatabase(request, response);
				break;
		}
	}
	
	private void addVote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if (isInputValid(request, response)) {
			try {
				User user = voteDbUtil.getUser(Long.parseLong(request.getParameter("phoneNumber")));

				if (user.getVoteCount() >= MAX_VOTES) {
					String message = "You have already voted " + MAX_VOTES + " times. You cannot vote any more";
					request.getSession().setAttribute("errorMessage", message);
				} else {
					Candidate candidate = new Candidate(Short.parseShort(request.getParameter("candidate")));
					voteDbUtil.addVote(new Vote(user, candidate));
					if (user.getVoteCount() == 0) {
						voteDbUtil.addUser(user);
					} else {
						voteDbUtil.updateUser(user);
					}
					String message = "Your vote has been submitted successfully";
					request.getSession().setAttribute("successMessage", message);
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		response.sendRedirect(request.getContextPath() + request.getServletPath());
	}
	
	private void getResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<Short, Long> voteResult = new HashMap<>();
		List<Candidate> candidateVotes = new ArrayList<>(); 
		
		try {
			voteResult = voteDbUtil.getVoteResult();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		long totalVotes = 0;
			
		for (Candidate candidate : candidates) {
			long votes = 0;
			if (voteResult.containsKey(candidate.getId())) {
				votes = voteResult.get(candidate.getId());
				totalVotes += votes;
			}
			candidate.setTotalVotes(votes);
			candidateVotes.add(candidate);
		}
			
		candidateVotes.sort(null);
			
		request.setAttribute("candidates", candidateVotes);
		request.setAttribute("total", totalVotes);
			
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vote-results.jsp");
		dispatcher.forward(request, response);
	}	
	
}

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

/*
 * Implements casting of votes and vote count. Mapped to web application root directory.
 */
@WebServlet("/")
public class VoteControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/* Maximal number of votes */
	public static final short MAX_VOTES = 3;
	
	/* Minimal and maximal length of the phone number */
	public static final short MIN_LENGTH = 7;
	public static final short MAX_LENGTH = 10; 
	
	/* Data source - parameters defined in the CONTEXT.XML */
	@Resource(name = "jdbc/vote_db")
	private DataSource dataSource;

	/* Database access */
    private VoteDbUtil voteDbUtil;
    
    /* List of candidates - does not change during the run of the servlet */
    private List<Candidate> candidates = new ArrayList<>();
       
    /* Servlet initialisation - gets database access reference and tries to get candidates.
     * When error - that means that database is empty and needs to be set up */
    public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		/* Maps database URL relative to the web application root directory */
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

    /* Serves GET requests */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (candidates.isEmpty()) {
			/* No candidates, the database is empty. Forward to setup page */
			RequestDispatcher dispatcher = request.getRequestDispatcher("/setup.jsp");
			dispatcher.forward(request, response);
		} else {		
			/* Read submit value. If not passed in, use default value */
			String command = request.getParameter("submit");
		
			if (command == null) {
				command = "getCandidates";
			}
			switch (command) {
				/* List all candidates and is ready to accept votes */
				case "getCandidates":
					getCandidates(request, response);
					break;
				/* Show vote results */	
				case "getResults":
					getResults(request, response);	
					break;
				default:
					getCandidates(request, response);
			}
		}	
	}
	
	/* Show all candidates */
	private void getCandidates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("candidates", candidates);
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vote.jsp");
		dispatcher.forward(request, response);
	}
	
	/* Set up database */
	private void setUpDatabase(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			voteDbUtil.setUpDatabase();
			candidates = voteDbUtil.getCandidates();
		} catch (Exception e) {
			throw new ServletException();
		}
		/* Use POST REDIRECT GET pattern */
		response.sendRedirect(request.getContextPath() + request.getServletPath());
	}
	
	/* Check user input when a vote is submitted */
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
	
	/* Serves POST requests */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String command = request.getParameter("submit");
		
		switch (command) {
		    /* Add a vote */
			case "addVote": 
				addVote(request, response);		
				break;
			/* Set up database */	
			case "setUp":
				setUpDatabase(request, response);
				break;
		}
	}
	
	/* Add a vote */
	private void addVote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if (isInputValid(request, response)) {
			try {
				User user = voteDbUtil.getUser(Long.parseLong(request.getParameter("phoneNumber")));

				/* Check vote count */
				if (user.getVoteCount() >= MAX_VOTES) {
					String message = "You have already voted " + MAX_VOTES + " times. You cannot vote any more";
					request.getSession().setAttribute("errorMessage", message);
				} else {
					Candidate candidate = new Candidate(Short.parseShort(request.getParameter("candidate")));
					/* Add vote */
					voteDbUtil.addVote(new Vote(user, candidate));
					/* Insert/update user's vote count */
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
		/* Use POST REDIRECT GET pattern */
		response.sendRedirect(request.getContextPath() + request.getServletPath());
	}
	
	/* Get current vote results */
	private void getResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<Short, Long> voteResult = new HashMap<>();
		List<Candidate> candidateVotes = new ArrayList<>(); 
		
		try {
			voteResult = voteDbUtil.getVoteResult();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		long totalVotes = 0;
		/* Create list of candidates */	
		for (Candidate candidate : candidates) {
			long votes = 0;
			if (voteResult.containsKey(candidate.getId())) {
				votes = voteResult.get(candidate.getId());
				totalVotes += votes;
			}
			candidate.setTotalVotes(votes);
			candidateVotes.add(candidate);
		}
		
		/* Sort candidates by number of votes descending and then by name */
		candidateVotes.sort(null);
			
		request.setAttribute("candidates", candidateVotes);
		request.setAttribute("total", totalVotes);
			
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vote-results.jsp");
		dispatcher.forward(request, response);
	}	
	
}

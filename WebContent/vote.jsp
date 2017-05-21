<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Vote - Cast Your Vote</title>
		<link type="text/css" rel="stylesheet" href="css/style.css" >
	</head>
	
	<body>
		<p class="error">${errorMessage}</p>
		<c:remove var="errorMessage" scope="session"/> 
		
		<p class="success">${successMessage}</p>
		<c:remove var="successMessage" scope="session"/> 
		
		<h2>Vote for Your Candidate!</h2>
		
		<form action="VoteControllerServlet" method="POST">
			
			<label for="phoneNumber">Phone number:</label>
			<input type="text" name="phoneNumber" id="phoneNumber" placeholder="Enter your phone number"><br>
			
			<br>
			
			<c:forEach var="candidate" items="${candidates}">
				<input type="radio" name="candidate" value="${candidate.id}"> ${candidate.name}<br>
			</c:forEach>
			
			<br>
			
			<button type="submit" name="submit" value="addVote">Vote</button>

		</form>
		
		<br>
		
		<form action="VoteControllerServlet" method="GET">
			<button type="submit" name="submit" value="getResults">Show Results</button>
		</form>
		
	</body>
	
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Vote - Results</title>
		<link type="text/css" rel="stylesheet" href="css/style.css" >
	</head>
	
	<body>
		
		<h2>Total votes: ${total}</h2>
		
		<table>
			<thead>
				<tr>
					<th>Candidate</th>
					<th>Number of votes</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="candidate" items="${candidates}">
				<tr>
					<td>${candidate.name}</td>
					<td>${candidate.totalVotes}</td>
				</tr>
				</c:forEach>
			</tbody>
		
		</table>
				
		<br>
		
		<a href="VoteControllerServlet">Back</a>

	</body>
</html>
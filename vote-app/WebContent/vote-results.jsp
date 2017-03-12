<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Vote - Results</title>
	</head>
	
	<body>
		
		<h2>Total votes: ${total}</h2>
		
		<c:forEach var="candidate" items="${candidates}">
			${candidate.name} ${candidate.totalVotes}<br>
		</c:forEach>
		
		<br>
		
		<a href="VoteControllerServlet">Back</a>

	</body>
</html>


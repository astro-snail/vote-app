<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Vote - Set Up</title>
		<link type="text/css" rel="stylesheet" href="css/style.css" >
	</head>
	
	<body>
	
		<h2>Set Up Database</h2>
			
		<form action="VoteControllerServlet" method="POST">
			<button type="submit" name="submit" value="setUp">Set Up</button>
		</form>
		
	</body>
</html>
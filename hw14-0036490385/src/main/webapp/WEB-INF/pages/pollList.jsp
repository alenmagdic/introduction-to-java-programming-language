<%@ page contentType = "text/html; charset = UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
	<h1>Polls</h1>
 	<p>List of available polls:</p>
	<ol>
	
	<c:forEach var = "poll" items="${polls}">
		<li><a href="glasanje?pollID=${poll.pollId}">${poll.title}</a></li>
	</c:forEach>
	
	</ol>
</body>
</html>
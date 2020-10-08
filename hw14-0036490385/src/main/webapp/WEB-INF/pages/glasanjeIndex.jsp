<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
	<h1>Poll: ${poll.title}</h1>
 	<p>${poll.message} Click on link to vote!</p>
	<ol>
	
	<c:forEach var = "option" items="${options}">
		<li><a href="glasanje-glasaj?pollID=${poll.pollId}&optionID=${option.id}">${option.title}</a></li>
	</c:forEach>
	
	</ol>
</body>
</html>
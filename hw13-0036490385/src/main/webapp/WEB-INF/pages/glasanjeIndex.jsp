<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">
	<h1>Favourite band poll:</h1>
 	<p>Among the given bands, what is your favourite band? Click on link to vote!</p>
	<ol>
	
	<c:forEach var = "band" items="${bands}">
		<li><a href="glasanje-glasaj?id=${band.id}">${band.name}</a></li>
	</c:forEach>
	
	</ol>
</body>
</html>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
	<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">
	<p><a href="setColor?color=white">WHITE</a><br>
	<a href="setColor?color=red">RED</a><br>
	<a href="setColor?color=green">GREEN</a><br>
	<a href="setColor?color=cyan">CYAN</a></p>
	</body>
</html>
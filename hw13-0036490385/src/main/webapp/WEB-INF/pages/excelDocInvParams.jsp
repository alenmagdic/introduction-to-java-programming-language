<%@ page session="true" contentType = "text/html; charset = UTF-8" pageEncoding="UTF-8" %>

<html>
	<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">
	<h1>Invalid parameters given</h1>
	<p> Expected parameters a,b in range [-100,100] and parameter n in range [1,5].
	</p>	
	</body>
</html>
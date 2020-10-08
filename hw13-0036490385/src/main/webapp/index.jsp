<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
	<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">
	<p><a href="colors.jsp">Background color chooser</a><br>
	
	<a href="trigonometric?a=0&b=90">Trigonometric functions</a></p>
	<form action="trigonometric" method="GET">
 	Start angle:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 	End angle:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 	<input type="submit" value="Get table"><input type="reset" value="Reset">
	</form>
	
	<p>
	<a href="stories/funny.jsp">Funny story</a><br>
	<a href="powers?a=1&b=100&n=3">Powers</a><br>
	<a href="appinfo.jsp">Application info</a><br>
	<a href="glasanje">Poll</a>
	</p>
	
	</body>
</html>
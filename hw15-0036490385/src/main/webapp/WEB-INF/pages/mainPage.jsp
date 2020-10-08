<%@ page session = "true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>

<header>
<%
	if(session.getAttribute("current.user.id")!=null) { 
		out.print("<h3>User: "+session.getAttribute("current.user.fn")+" "+session.getAttribute("current.user.ln"));
		%> <a href="/blog/servleti/logout">Logout</a>  <a href="/blog/servleti/author/<% out.print(session.getAttribute("current.user.nick"));  %>   ">My blog</a>
		<a href="/blog/servleti/main" >Home</a> </h3>
	<%}else {
		 %>
		 <h3>User: Not logged in</h3>
	<%}%>

</header>

<%
	if(session.getAttribute("current.user.id")==null) { %>
	
		<form action="main" method="post">
 			 Nick:<br>
  			<input type="text" name="nick" value="${nick}" required>
  			<br>
  			Password:<br>
  			<input type="password" name="password" value="" required>
  			<br><b>${invalidDataMsg}</b>
  			<br><br>
  			<input type="submit" value="Login">
		</form>
		<p><a href="register">Register</a></p>
	<%}%>



<h2>Blogers:</h2>

<c:forEach var="author" items="${authors}">
	<a href="author/${author.nick}">${author.nick}</a><br>
</c:forEach>
</body>
</html>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
		 <h3>User: Not logged in <a href="/blog/servleti/main">Home</a></h3>
	<%}%>

</header>

<form action="/blog/servleti/author/editentry" method="post">
 	Title:<br>
 	<input type="text" name="title" value="${blogEntry.title}">
 		<br>
 		<textarea name="text" rows="20" cols="300">${blogEntry.text}</textarea>
 		
 	<% 
	if(request.getAttribute("blogEntry") != null) {
		 %><input type="hidden" name="entry_id" value="${blogEntry.id}"><%
	}%>
	
  	<input type="submit" value="Submit">
</form> 

</body>
</html>
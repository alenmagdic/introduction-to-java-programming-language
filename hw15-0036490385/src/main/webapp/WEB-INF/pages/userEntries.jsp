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
		 <h3>User: Not logged in <a href="/blog/servleti/main">Home</a></h3>
	<%}%>

</header>


	
<h1>Entries:</h1>

<%
	if(session.getAttribute("current.user.id")!=null && session.getAttribute("current.user.nick").equals(request.getAttribute("nick"))) { %>
		<h3><a href="/blog/servleti/author/${nick}/new">Add new entry</a><br></h3>
		
	<%}%>

<%
	if((Boolean)request.getAttribute("noEntries")) {
		out.print("There are no any entries.");
	} else {%>
		<c:forEach var="ent" items="${entries}">
			<a href="/blog/servleti/author/${nick}/${ent.id}">${ent.title}</a><br>
		</c:forEach>
	<%}%>


</body>
</html>
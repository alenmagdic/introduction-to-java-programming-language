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

<h2>${blogEntry.title}</h2>
<p>${blogEntry.text}</p>

<h6>Author: <a href="/blog/servleti/author/${blogEntry.creator.nick}" >${blogEntry.creator.nick}</a> Created at: ${blogEntry.createdAt} Last modified at: ${blogEntry.lastModifiedAt}</h6>

<%
	if(session.getAttribute("current.user.id")!=null && session.getAttribute("current.user.nick").equals(request.getAttribute("authorNick"))) { %>
		<h3><a href="/blog/servleti/author/${authorNick}/edit?entry_id=${blogEntry.id}">Edit entry</a><br></h3>
		
	<%}%>

<c:if test="${!blogEntry.comments.isEmpty()}">
   <ul>
   	<c:forEach var="e" items="${blogEntry.comments}">
   	<li><div style="font-weight: bold">[User=<c:out value="${e.usersEMail}"/>] <c:out value="${e.postedOn}"/></div><div style="padding-left: 10px;"><c:out value="${e.message}"/></div></li>
   </c:forEach>
   </ul>
</c:if>

<h3>Add new comment:</h3>

<form action="/blog/servleti/blog_entry" method="post">
 	Email:<br>
 	<input type="text" name="email" value="" required>
 	<br>
 	<textarea name="text" rows="5" cols="70"></textarea>

	<input type="hidden" name="entry_id" value="${blogEntry.id}">
	<br>
  	<input type="submit" value="Add comment">
</form> 

</body>
</html>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">
	<h1>Application running time:</h1>
	<%
		long time =  System.currentTimeMillis() - (long)session.getServletContext().getAttribute("startuptime");
		long days = time/(24*60*60*1000);
		long hours = (time/(60*60*1000))%24;
		long minutes = (time/(60*1000))%60;
		long seconds = (time/1000)%60;
		out.print(days+" days "+hours+" hours "+minutes+" minutes "+seconds+" seconds");
	%>
	
</body>
</html>
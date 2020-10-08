<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
 <head>
 <style type="text/css">
 table.rez td {text-align: center;}
 </style>
 </head>
<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">

 <h1>Poll results</h1>
 <p>This are the poll results.</p>
 <table border="1" cellspacing="0" class="rez">
 <thead><tr><th>Band</th><th>Votes</th></tr></thead>
 <tbody>
 	<c:forEach var = "res" items="${voteresults}">
		<tr><td>${res.band.name}</td><td> ${res.votes} </td></tr>
	</c:forEach>
 </tbody>
 </table>

 <h2>Graphical representation of the results</h2>
 <img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

 <h2>Results in XLS format</h2>
 <p>Results in XLS format can be downloaded <a href="glasanje-xls">here</a></p>

 <h2>Songs</h2>
 <p>Song example/s of the poll winning band/s:</p>
 <ul>
<c:forEach var = "band" items="${winners}">
		<li><a href="${band.songLink}" target="_blank">${band.name}</a></li>
	</c:forEach>
 </ul>
 </body>
</html>
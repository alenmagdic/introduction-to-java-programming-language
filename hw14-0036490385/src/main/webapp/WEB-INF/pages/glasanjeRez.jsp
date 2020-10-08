<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
 <head>
 <style type="text/css">
 table.rez td {text-align: center;}
 </style>
 </head>
<body>

 <h1>Poll results</h1>
 <p>This are the poll results.</p>
 <table border="1" cellspacing="0" class="rez">
 <thead><tr><th>Option</th><th>Votes</th></tr></thead>
 <tbody>
 	<c:forEach var = "option" items="${voteresults}">
		<tr><td>${option.title}</td><td> ${option.votesCount} </td></tr>
	</c:forEach>
 </tbody>
 </table>

 <h2>Graphical representation of the results</h2>
 <img alt="Pie-chart" src="glasanje-grafika?pollID=${pollID}" width="400" height="400" />

 <h2>Results in XLS format</h2>
 <p>Results in XLS format can be downloaded <a href="glasanje-xls?pollID=${pollID}"">here</a></p>

 <h2>More about winner/s</h2>
 <p> Here you can find some information (picture,video,etc.) about winner/s: </p>
 <ul>
<c:forEach var = "opt" items="${winners}">
		<li><a href="${opt.link}" target="_blank">${opt.title}</a></li>
	</c:forEach>
 </ul>

 </body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">
<table border="1">
	<tr><th>x</th><th>sin(x)t</th><th>cos(x)</th></tr>
	<c:forEach var = "item" items="${listOfResults}">
		<tr><td>${item.number}</td><td>${item.sinValue}</td> <td>${item.cosValue}</td></tr>
	</c:forEach>
	</table>
</body>
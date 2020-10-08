<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>

<body style="background-color: <%out.print(session.getAttribute("pickedBgCol"));%>;">

<h1> OS usage  </h1>
<p>Here are the results of OS usage in  survey that we completed.</p>

<img src="reportimage" alt="Pie chart">


</body>


</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<header>
<h3><a href="/blog/servleti/main">Home</a></h3>
</header>

<h2>Register form:</h2>
<form action="register" method="post">
  First name:<br>
  <input type="text" name="firstName" value="${firstName}" required>
  <br>
  Last name:<br>
  <input type="text" name="lastName" value="${lastName}" required>
  <br>
  Email:<br>
  <input type="text" name="email" value="${email}" required>
  <br>
  Nick:<br>
  <input type="text" name="nick" value="${nick}" required>
  <b>${nickErrorMsg}</b><br>
  Password:<br>
  <input type="password" name="password" value="" required>
  <br><br>
  <input type="submit" value="Register">
</form> 

<a href="/blog/servleti/main">Home</a>

</body>
</html>
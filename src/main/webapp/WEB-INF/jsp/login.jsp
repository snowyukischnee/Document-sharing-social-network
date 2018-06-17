<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: TuanNH
  Date: 6/12/2018
  Time: 3:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Login</h1>
<form action="perform_login" method='POST'>
    User:<input type='text' name='username' value=''>
    Password:<input type='password' name='password' />
    <input name="submit" type="submit" value="submit" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<h1>Register</h1>
<form action="register" method='POST'>
    User:<input type='text' name='username' value=''>
    Password:<input type='password' name='password' />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input name="submit" type="submit" value="submit" />
</form>
</body>
</html>

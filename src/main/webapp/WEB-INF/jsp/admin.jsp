<%--
  Created by IntelliJ IDEA.
  User: TuanNH
  Date: 6/21/2018
  Time: 10:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Register</h1>
<form action="admin/register" method='POST'>
    User:<input type='text' name='username' value=''>
    Password:<input type='password' name='password' />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input name="submit" type="submit" value="submit" />
</form>
</body>
</html>

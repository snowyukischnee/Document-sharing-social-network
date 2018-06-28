<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: TuanNH
  Date: 6/12/2018
  Time: 3:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="true"%>
<html>
<head>
    <title>Title</title>
    <script src="/resources/js/index.js"></script>
</head>
<body>
    <h1>Welcome ${pageContext.request.userPrincipal.name}</h1>
    <form action="upload-gg/?${_csrf.parameterName}=${_csrf.token}" method="POST" enctype="multipart/form-data">
        Upload<input type="file" name="files" accept=".pdf" multiple=""/>
        <input type="submit" name="submit" value="upload"/>
    </form>
    <br>
    <a href="/list-gg">List all uploaded files</a>
    <br>
    <form action="logout" method="POST">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" name="submit" value="logout"/>
    </form>
    Changepass
    <form action="changepass" method='POST'>
        Password:<input type='password' name='password' />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input name="submit" type="submit" value="submit" />
    </form>
    gg
    <c:forEach items="${arr}" var="item">
        <tr>
            <td>
                ${item}
            </td>
        </tr>
    </c:forEach>
</body>
</html>

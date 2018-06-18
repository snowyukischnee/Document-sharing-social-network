<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: TuanNH
  Date: 6/18/2018
  Time: 2:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List files</title>
</head>
<body>
    <h1>Welcome ${pageContext.request.userPrincipal.name}</h1>
    <h2>Files uploaded</h2>
    <table>
    <c:forEach items="${files}" var="item">
        <tr>
            <td>
                <a href="/download?file=${item.getName()}">
                    ${item.getName()}
                </a>
            </td>
        </tr>
    </c:forEach>
    </table>
</body>
</html>

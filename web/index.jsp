<%@ page import="java.io.IOException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
   <%
       RequestDispatcher rd = request.getRequestDispatcher("HikePage/Home");
       rd.forward(request, response);
   %>

</body>
</html>

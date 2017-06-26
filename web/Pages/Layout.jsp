<%@ page import="Models.MiniUser" %>
<%@ page import="Servlets.Helper" %><%--
  Created by IntelliJ IDEA.
  User: Levani
  Date: 13.06.2017
  Time: 13:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title></title>
    <link rel="stylesheet" href="../Content/css/normalize.css">
    <link rel="stylesheet" href="../Content/css/font-awesome.min.css">
    <link rel="stylesheet" href="../Content/css/common.css">
    <link rel="stylesheet" href="../Content/css/login-page.css">
    <link rel="stylesheet" href="../Content/css/main.css">
</head>
<body>
<%
    String pageName = (String)request.getAttribute("page");
    MiniUser loggedInUser = (MiniUser)request.getAttribute("loggedInUser");
%>
<% if(!pageName.equals("LoginPage.jsp")){ %>
<script>
    var user = {
        id : '<%= loggedInUser.getId() %>',
        firstName: '<%= loggedInUser.getFirstName()%>',
        lastName: '<%= loggedInUser.getLastName() %>',
        profilePictureAddress: '<%= loggedInUser.getProfilePictureAddress() %>'
    };
</script>
<% } %>
<div class="wrapper clearfix">
    <% if(!pageName.equals("LoginPage.jsp")){ %>
    <header>
        <div class="header-left">
            <div class="logo-block">
                <a class="logo-link" href="/Home">
                    <img src="../Content/img/logo.png" alt="" class="logo-img">
                </a>
            </div>
        </div>
        <div class="header-right">
            <div class="profile-block">

                <a href="#" class="profile-link">
                    <div class="avatar-block">

                    </div>
                    <div class="hidden logged-user-id"><%=loggedInUser.getId()%></div>
                    <div class="profile-name">
                        <%=loggedInUser.getFirstName()%> <%=loggedInUser.getLastName()%>
                    </div>
                </a>
                <div class="logout-block"><a href="#">Log out</a></div>
            </div>
        </div>

    </header>
    <% } %>
    <jsp:include page='<%= pageName %>' />
    <script>document.querySelectorAll("title")[0].innerHTML = document.querySelectorAll("setTitle")[0].innerHTML;</script>
</div>
<footer>
    <div class="footer-info"> All Rights Reserved  © HikingPartners.ge  2017</div>
</footer>

</body>
</html>

<%@ page import="Servlets.Helper" %>
<%@ page import="Models.User" %><%--
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
    <link rel="stylesheet" href="../Content/css/main.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat|Quicksand" rel="stylesheet">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAN41T3N0B5Tx61omm8n9ZX6quK4FvG1jk" type="text/javascript"></script>
</head>
<body>
<%
    String pageName = (String)request.getAttribute("page");
    User loggedInUser = (User)request.getAttribute("loggedInUser");
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

                <a href="/Profile?userID=<%=loggedInUser.getId()%>" class="profile-link">
                    <div class="avatar-block" style="background-image: url(<%= loggedInUser.getProfilePictureAddress() %>) ">

                    </div>
                    <div class="hidden logged-user-id"><%=loggedInUser.getId()%></div>
                    <div class="profile-name">
                        <%=loggedInUser.getFirstName()%> <%=loggedInUser.getLastName()%>
                    </div>
                </a>
                <div class="logout-block"><a href="/Logout">Log out</a></div>
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

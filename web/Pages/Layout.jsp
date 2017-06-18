<%--
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
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="../Content/css/main.css">
    <script src="../Scripts/vue.min.js"></script>
</head>
<body>
<div class="wrapper clearfix">
    <header>
        <div class="header-left">
            <div class="logo-block">
                <a class="logo-link" href="#">
                    <img src="../Content/img/logo.png" alt="" class="logo-img">
                </a>
            </div>
        </div>
        <div class="header-right">
            <div class="profile-block">
                <a href="#" class="profile-link">
                    <div class="avatar-block">

                    </div>
                    <div class="profile-name">
                        Sandro Jikia
                    </div>
                </a>
                <div class="logout-block"><a href="#">Log out</a></div>
            </div>
        </div>

    </header>
    <jsp:include page='<%=(String)request.getAttribute("page")%>' />
</div>
<footer></footer>
</body>
</html>

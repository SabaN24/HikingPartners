<%@ page import="Database.DataManager" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="Models.Hike.AboutModel" %><%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 12-Jun-17
  Time: 01:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="../Content/css/normalize.css">
    <link rel="stylesheet" href="../Content/css/main.css">
</head>
<body>
<div class="wrapper">
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
                <a href="" class="profile-link">
                    <div class="avatar-block">

                    </div>
                    <div class="profile-name">Sandro Jikia</div>
                </a>
            </div>
        </div>


    </header>
    <aside>
        <div class="creator-block">
            <div class="avatar-block">

            </div>
            <div class="name-block">
                <div class="name-text">Saba Natr.</div>
                <div class="role-block">Creator</div>
            </div>
        </div>
        <nav>
            <ul class="nav-list">
                <li class="nav-item active"><a href="#" class="nav-link">Hike Page</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Items</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Members</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Gallery</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Locations</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Feed</a></li>
            </ul>
        </nav>
    </aside>
    <main>
        <div class="main-content">
            <div class="slider-block">
                <div class="caption"> ქვაბისხევი / KVABISKHEVI</div>
            </div>
            <div class="description-block">
                <div class="description-wrapper">
                    <div class="description-header">
                        <%
                            ServletContext sc = request.getServletContext();
                            AboutModel aboutModel = ((DataManager)sc.getAttribute(DataManager.ATTR)).getAboutModel(1);
                            out.print(" <div class=\"description-header__left\">" + aboutModel.getName() + "</div>");
                            out.print(" <div class=\"description-header__right\">" + aboutModel.getStartDate() + " - " + aboutModel.getEndDate() + "</div>");
                            //out.print(" <div class=\"description-header__right\">" + aboutModel.getMaxPeople() + "</div>");
                        %>
                    </div>
                    <div class="description-body">
                        <%
                            out.print(aboutModel.getDescription());
                        %>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
<footer></footer>

<script src="https://unpkg.com/vue@2.3.4"></script>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            message: 'Hello world!'
        }
    });
</script>
</body>
</html>
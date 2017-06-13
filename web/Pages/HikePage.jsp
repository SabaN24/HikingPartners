<%@ page import="Database.DataManager" %>
<%@ page import="Models.Comment" %>
<%@ page import="Models.Hike.AboutModel" %>
<%@ page import="Models.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %><%--
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
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="../Content/css/main.css">
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
                            AboutModel aboutModel = (AboutModel) request.getAttribute(AboutModel.ATTR);
                            out.print(" <div class=\"description-header__left\">" + aboutModel.getName() + "</div>");
                            out.print(" <div class=\"description-header__right\">" + aboutModel.getStartDate() + " - " + aboutModel.getEndDate() + "</div>");
                            //out.print(" <div class=\"description-header__right\">" + aboutModel.getMaxPeople() + "</div>");
                        %>
                    </div>
                    <div class="description-body">
                        <div style="margin-bottom: 10px;font-weight:bold;">Description:</div>

                        <%
                            out.print(aboutModel.getDescription());
                        %>
                    </div>
                </div>

                <div class="comments-block">
                    <div class="comments-block-inner">
                        <ul class="comments-list">

                            <%

                                List<Comment> comments = aboutModel.getComments();
                                for(Comment comment : comments){
                                    //User user = comment.getUser();
                                    String time = comment.getDate().toString();
                                    out.println("<li class=\"comment\">");
                                    out.println("<div class=\"avatar-block\"></div>");
                                    out.println("<div class=\"comment-info\">");
                                    out.println("<div class=\"comment-info__upper\">");
                                    out.println("<div class=\"comment-author\">" +  "Nodari" + " " + "Sairmeli"  + "</div>");
                                    out.println("</div>");
                                    out.println("<div class=\"comment-info__lower\">");
                                    out.println("<div class=\"comment-time\">" + time + "</div>");
                                    out.println("<div class=\"like-block\">");
                                    out.println("<i class=\"fa fa-thumbs-up\" aria-hidden=\"true\"></i>" + comment.getLikeNUmber());
                                    out.println("</div>");
                                    out.println("</div>");
                                    out.println("</div>");
                                    out.println("<div class=\"comment-text\">" + comment.getComment() + "</div>");
                                    out.println("</li>");
                                }
                            %>

                        </ul>
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
<%@ page import="Models.Comment" %>
<%@ page import="Models.Hike.AboutModel" %>
<%@ page import="Models.Hike.DefaultModel" %>
<%@ page import="Models.MiniUser" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 12-Jun-17
  Time: 01:18
  To change this template use File | Settings | File Templates.
--%>
<setTitle>
    <%
        AboutModel aboutModel = (AboutModel) request.getAttribute(AboutModel.ATTR);
        out.print(aboutModel.getName());
    %>
</setTitle>

</header>
<aside>
    <div class="creator-block">
        <div class="avatar-block">

        </div>
        <div class="name-block">
            <div class="name-text">
                <%
                    DefaultModel defaultModel = (DefaultModel)request.getAttribute(DefaultModel.ATTR);
                    MiniUser creator = defaultModel.getCreator();
                    out.print(creator.getFirstName() + " " + creator.getLastName());
                %>
            </div>
            <div class="role-block">Creator</div>
        </div>
    </div>
    <nav>
        <ul class="nav-list">
            <li class="nav-item active"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Hike Page</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Items</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Members</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Gallery</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Locations</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Feed</a></li>
        </ul>
    </nav>
</aside>
<main>
    <div class="main-content">
        <div class="slider-block">
            <div class="caption">
                <%=  defaultModel.getCoverPhotos().get(0).getLocationName() %>
            </div>
        </div>
        <div class="description-block">
            <div class="description-wrapper">
                <div class="description-header">
                    <div class="description-header__left"> <%=aboutModel.getName()%> </div>
                    <div class="description-header__right">
                        <span title="ადამიანების მაქსიმალური რაოდენობა" style="margin-right: 30px;"><i class="fa fa-user" aria-hidden="true"></i> <%= aboutModel.getMaxPeople() %> </span>
                        <span title="გამგზავრების თარიღი" style="margin-right: 20px;"><i class="fa fa-arrow-up" aria-hidden="true"></i> <%= aboutModel.getStartDate() %></span>
                        <span title="ჩამოსვლის თარიღი"><i class="fa fa-arrow-down" aria-hidden="true"></i> <%= aboutModel.getEndDate() %></span>

                    </div>
                    <%--out.print(" <div class=\"description-header__right\">" + aboutModel.getMaxPeople() + "</div>");--%>
                </div>
                <div class="description-body">
                    <div style="margin-bottom: 10px;font-weight:bold;">Description:</div>
                    <%= aboutModel.getDescription() %>
                </div>
            </div>
            <div class="comments-count"><%= aboutModel.getComments().size() %> comments.</div>
            <div class="comments-block">
                <div class="comments-block-inner">
                    <div class="comment">
                        <div class="add-comment">
                            <div class="avatar-block">
                            </div>
                            <form action="HikePageServlet" method="post">
                                <input class="comment-input" type="text" name="add-comment" placeholder="Write comment...">
                            </form>
                        </div>
                    </div>
                    <ul class="comments-list">

                        <%

                            List<Comment> comments = aboutModel.getComments();
                            for (Comment comment : comments) {
                                //User user = comment.getUser();
                                String time = comment.getDate().toString();
                        %>
                        <li class="comment">
                            <div class="avatar-block"></div>
                            <div class="comment-info">
                                <div class="comment-info__upper">
                                    <div class="comment-author">
                                        <%
                                            MiniUser author = comment.getUser();
                                            out.print(author.getFirstName() + " " + author.getLastName());
                                        %>
                                    </div>
                                </div>
                                <div class="comment-info__lower">
                                    <div class="comment-time"><%= comment.getDate().toString() %>
                                    </div>
                                    <div class="like-block">
                                        <i class="fa fa-thumbs-up" aria-hidden="true"></i>
                                        <%= comment.getLikeNUmber() %>
                                    </div>
                                </div>
                            </div>
                            <div class="comment-text"><%= comment.getComment() %>
                            </div>
                        </li>
                        <%
                            }
                        %>

                    </ul>
                </div>
            </div>
        </div>
    </div>
</main>
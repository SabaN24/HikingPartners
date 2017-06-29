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
<div id="vueapp">
    <script>
        var hikeId = <%= Integer.parseInt(request.getParameter("hikeId"))%>;
    </script>
    <setTitle>
        <%
            DefaultModel defaultModel = (DefaultModel) request.getAttribute(DefaultModel.ATTR);
            int hikeId = defaultModel.getId();
            String fullSubPage = (String) request.getAttribute("subPage");
            String subPage = fullSubPage.substring(9, fullSubPage.length() - 4);
            MiniUser creator = defaultModel.getCreator();

            out.print(defaultModel.getName());
        %>
    </setTitle>

    </header>
    <aside>
        <div class="creator-block">
            <div class="avatar-block" style="background-image: url(<%= creator.getProfilePictureAddress() %>)">

            </div>
            <div class="name-block">
                <div class="name-text">
                    <%= creator.getFirstName() + " " + creator.getLastName() %>
                </div>
                <div class="role-block">Creator</div>
            </div>
        </div>
        <nav>
            <ul class="nav-list">
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Home' }"><a href='<%= "/HikePage/Home?hikeId=" + hikeId%>' class="nav-link"><i
                        class="fa fa-home fa-pages"></i> Hike
                    Page</a></li>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Items' }"><a href="#"
                                                                                               class="nav-link"><i
                        class="fa fa-list-ul fa-pages"></i> Items</a></li>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>'== 'Members' }"><a href="#"
                                                                                                class="nav-link"><i
                        class="fa fa-users fa-pages"></i> Members</a></li>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Gallery' }"><a href="#"
                                                                                                 class="nav-link"><i
                        class="fa fa-picture-o fa-pages"></i> Gallery</a>
                </li>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Locations' }"><a href="#"
                                                                                                   class="nav-link"><i
                        class="fa fa-map-marker fa-pages"></i>
                    Locations</a></li>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Feed' }"><a href='<%= "/HikePage/Feed?hikeId=" + hikeId%>' class="nav-link"><i
                        class="fa fa-rss-square fa-pages"></i> Feed</a>
                </li>
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


        </div>

        <script src="../Scripts/axios.min.js"></script>
        <script src="../Scripts/vue.min.js"></script>
        <jsp:include page='<%= fullSubPage %>'/>

    </main>
</div>
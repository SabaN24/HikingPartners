<%@ page import="Models.Comment" %>
<%@ page import="Models.Hike.AboutModel" %>
<%@ page import="Models.Hike.DefaultModel" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.User" %>
<%@ page import="Database.MainDM" %>
<%@ page import="Models.Member" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 12-Jun-17
  Time: 01:18
  To change this template use File | Settings | File Templates.
--%>
<script>
    var hikeId = <%= Integer.parseInt(request.getParameter("hikeId"))%>;
</script>
<setTitle>
    <%
        DefaultModel defaultModel = (DefaultModel) request.getAttribute(DefaultModel.ATTR);
        int hikeId = defaultModel.getId();
        List<Member> hikeMembers = MainDM.getInstance().getHikeMembers(hikeId);
        Integer loggedInUser = (Integer) request.getSession().getAttribute("userID");
        int loggedInUserId = loggedInUser;
        boolean loggedInUserIsMember = false;
        for(int i = 0; i < hikeMembers.size(); i++){
            if(hikeMembers.get(i).getId() == loggedInUserId) {
                loggedInUserIsMember = true;
            }
        }
        String fullSubPage = (String) request.getAttribute("subPage");
        String subPage = fullSubPage.substring(9, fullSubPage.length() - 4);
        User creator = defaultModel.getCreator();
        out.print(defaultModel.getName());



        boolean IdMatch = (loggedInUserId == defaultModel.getCreator().getId());
        String hikeName = defaultModel.getName();

    %>
</setTitle>
<script>
    document.getElementsByTagName("title")[0].innerHTML = document.getElementsByTagName("setTitle")[0].innerHTML;
</script>

<div id="profilePopupVue"
     v-if=" '<%= subPage %>' == 'Feed' || '<%= subPage %>' == 'Home' || '<%= subPage %>' == 'Members' "
     class="profile-popup-wrapper" :class="{ active : profPopupActive, up : popupUp }"
     @mouseleave="profPopupActive = false">
    <div class="profile-popup">
        <div class="profile-popup-cover bg"
             :style="{ backgroundImage: 'url(' + hoveredUser.coverPictureAddress + ')' }">
            <div class="profile-popup-name">{{hoveredUser.firstName}} {{hoveredUser.lastName}}</div>
        </div>
        <div class="profile-popup-prof-pic bg"
             :style="{ backgroundImage: 'url(' + hoveredUser.profilePictureAddress + ')' }"></div>
        <div class="profile-popup-info">
            <div><i class="fa fa-birthday-cake" aria-hidden="true"></i>Birthday: {{hoveredUser.birthDate ?
                hoveredUser.birthDate : "hidden"}}
            </div>
            <div><i class="fa fa-envelope" aria-hidden="true"></i>Email: {{hoveredUser.email}}</div>
        </div>
        <div class="profile-popup-nav"
             v-show="hoveredUser.id != <%= ((User)request.getAttribute("loggedInUser")).getId() %>">
            <a class="mybtn profile-popup-btn" :href="'/Profile?userID=' + hoveredUser.id"><i class="fa fa-user"
                                                                                              aria-hidden="true"></i>Go
                To Profile</a>
            <button class="mybtn profile-popup-btn" v-on:click="openConversation(hoveredUser.id)"><i
                    class="fa fa-commenting" aria-hidden="true"></i>Message
            </button>
        </div>
    </div>
</div>

<div>


    <aside>
        <div class="creator-block" onclick="window.location = '/Profile?userID=<%= creator.getId()%>'">
            <div class="avatar-block" style="background-image: url(<%= creator.getProfilePictureAddress() %>) ">

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
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Home' }">
                    <a href='<%= "/HikePage/Home?hikeId=" + hikeId%>' class="nav-link">
                        <i class="fa fa-home fa-pages"></i> About
                    </a>
                </li>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>'== 'Members' }">
                    <a href="<%= "/HikePage/Members?hikeId=" + hikeId%>" class="nav-link">
                        <i class="fa fa-users fa-pages"></i> Members
                    </a>
                </li>
                <%
                    if(loggedInUserIsMember){%>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Gallery' }">
                    <a href="<%= "/HikePage/Gallery?hikeId=" + hikeId%>" class="nav-link">
                        <i class="fa fa-picture-o fa-pages"></i> Gallery
                    </a>
                </li>
                <%}%>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Locations' }">
                    <a href="<%= "/LocationsServlet?hikeId=" + hikeId%>" class="nav-link">
                        <i class="fa fa-map-marker fa-pages"></i> Locations
                    </a>
                </li>
                <%
                    if(loggedInUserIsMember){%>
                <li class="nav-item" v-bind:class="{ active: '<%= subPage %>' == 'Feed' }">
                    <a href='<%= "/HikePage/Feed?hikeId=" + hikeId%>' class="nav-link">
                        <i class="fa fa-rss-square fa-pages"></i> Feed
                    </a>
                </li>
                <%}%>
            </ul>
        </nav>
    </aside>
    <main>
        <div class="main-content">

                <div id="coverVue" class="slider-block" v-bind:style="{ backgroundImage: 'url(' + coverPhoto + ')' }">
                    <div class="caption">
                        <%=  defaultModel.getCoverPhotos().get(0).getDescription() %>
                        <form action="" onsubmit="return false;" id="form-cover"></form>
                        <div  v-if="isCreator" v-if="!uploadingCover" class="upload-image-button cover-image-button " @click="chooseCover()" ></div>
                        <div v-if="uploadingCover">
                            <button @click="saveCover()">Save Cover</button>
                            <button @click="removeCover()">Remove Cover</button>
                        </div>
                    </div>
                </div>
        </div>

            <script>
                var profilePopupVue = new Vue({
                    el: "#profilePopupVue",
                    data: {
                        profPopupActive: false,
                        popupUp: false,
                        hoveredUser: {}
                    },
                    methods: {
                        hoverUser: function (user, e) {
                            if (this.profPopupActive) return;
                            this.hoveredUser = user;
                            this.profPopupActive = true;
                            var rect = e.target.getBoundingClientRect();
                            var popup = document.querySelector('.profile-popup-wrapper');
                            popup.style.left = rect.left + pageXOffset + 'px';
                            popup.style.top = rect.top + pageYOffset + e.target.clientHeight - 5 + 'px';
                            console.log(e);
                            console.log(window.innerHeight);
                            this.popupUp = false;
                            if (window.innerHeight - e.clientY < 300) {
                                this.popupUp = true;
                                popup.style.top = rect.top + pageYOffset - 275 + 'px';
                            }
                        },
                        hoverOutUser: function (e) {
                            if (!this.profPopupActive) return;
                            if (document.querySelectorAll(".profile-popup-wrapper:hover").length) return;
                            this.profPopupActive = false;

                        },
                        openConversation: function (userId) {
                            appChat.openChat(userId);
                        }

                    }
                });

                var coverVue = new Vue({
                    el: "#coverVue",
                    data: {
                        coverPhoto: "",
                        coverPhotoBackUp: "",
                        uploadingCover: false,
                        isCreator: true,
                    },
                    created: function ()  {
                        console.log(this.coverPhoto);
                        this.coverPhoto= "<%=defaultModel.getCoverPhotos().get(defaultModel.getCoverPhotos().size()-1).getSrc()%>";
                        this.coverPhotoBackUp = this.coverPhoto;
                        console.log(this.coverPhoto);
                        this.isCreator = <%=IdMatch%>;
                    },
                    methods: {
                        chooseCover: function () {
                            var input = document.createElement("input");
                            var self = this;



                            input.setAttribute("name", "cover");
                            input.setAttribute("type", "file");
                            input.setAttribute("style", "display:none;");
                            input.setAttribute("accept", "image/*");
                            document.querySelector("#form-cover").insertBefore(input, document.querySelector("#form-cover").children[0]);

                            var descriptionInput = document.createElement("input");
                            descriptionInput.setAttribute("name", "descriptions");
                            descriptionInput.setAttribute("type", "hidden");
                            descriptionInput.setAttribute("value", "");
                            document.querySelector("#form-cover").insertBefore(descriptionInput, document.querySelector("#form-cover").children[0]);

                            input.click();
                            input.onchange = function (event) {
                                var input = event.target;
                                if (input.files && input.files[0]) {
                                    var reader = new FileReader();
                                    reader.onload = function (e) {
                                        self.coverPhoto = e.target.result;
                                        self.uploadingCover = true;
                                    };
                                    reader.readAsDataURL(input.files[0]);
                                }
                            }
                        },

                        saveCover: function(){
                            var th = this;
                            th.coverPhotoBackUp = th.coverPhoto;
                            th.uploadingCover = false;
                            axios.post('/UploadCover?hikeID=' + <%= hikeId %>, new FormData(document.querySelector("#form-cover")));
                        },

                        removeCover: function () {
                            var th = this;
                            th.uploadingCover =false;
                            th.coverPhoto = th.coverPhotoBackUp;
                            th.uploadingCover = false;
                        }

                    }

                });

            </script>

<div  id="vueapp">
            <jsp:include page='<%= fullSubPage %>'/>

    </main>

</div>
<%@ page import="Models.Comment" %>
<%@ page import="Models.Hike.AboutModel" %>
<%@ page import="Models.Hike.DefaultModel" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.User" %>
<%@ page import="Database.MainDM" %>
<%@ page import="Models.Member" %>
<%@ page import="Models.Photo" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 12-Jun-17
  Time: 01:18
  To change this template use File | Settings | File Templates.
--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.min.js"></script>
<script src="/Scripts/slick.min.js"></script>
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
        for (int i = 0; i < hikeMembers.size(); i++) {
            if (hikeMembers.get(i).getId() == loggedInUserId) {
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
            <%! private String isActive(String curPage, String page) {
                String res = curPage.equals(page) ? " active" : "";
                return res;
            } %>
            <ul class="nav-list">
                <li class="nav-item<%=isActive(subPage, "Home") %>">
                    <a href='<%= "/HikePage/Home?hikeId=" + hikeId%>' class="nav-link">
                        <i class="fa fa-home fa-pages"></i> About
                    </a>
                </li>
                <li class="nav-item<%=isActive(subPage, "Members") %>">
                    <a href="<%= "/HikePage/Members?hikeId=" + hikeId%>" class="nav-link">
                        <i class="fa fa-users fa-pages"></i> Members
                    </a>
                </li>
                <%
                    if (loggedInUserIsMember) {%>
                <li class="nav-item<%=isActive(subPage, "Gallery") %>">
                    <a href="<%= "/HikePage/Gallery?hikeId=" + hikeId%>" class="nav-link">
                        <i class="fa fa-picture-o fa-pages"></i> Gallery
                    </a>
                </li>
                <%}%>
                <li class="nav-item<%=isActive(subPage, "Locations") %>">
                    <a href="<%= "/LocationsServlet?hikeId=" + hikeId%>" class="nav-link">
                        <i class="fa fa-map-marker fa-pages"></i> Locations
                    </a>
                </li>
                <%
                    if (loggedInUserIsMember) {%>
                <li class="nav-item<%=isActive(subPage, "Feed") %>">
                    <a href='<%= "/HikePage/Feed?hikeId=" + hikeId%>' class="nav-link">
                        <i class="fa fa-rss-square fa-pages"></i> Feed
                    </a>
                </li>
                <%}%>
            </ul>
        </nav>
    </aside>
    <main>
        <div class="main-content slider-container" id="coverVue">
            <div class="slick">
                <%
                    int i = 0;
                    if (defaultModel.getCoverPhotos().size() == 0) {%>

                <div class="slider-block">
                    <div class="caption">
                    </div>
                </div>

                <% } else {
                    for (Photo photo : defaultModel.getCoverPhotos()) {%>

                <div class="slider-block" style="background-image: url(<%=photo.getSrc()%>)">
                    <div class="caption">
                        <span>
                            <%=  photo.getDescription() %>
                            <% if (defaultModel.getCreator().getId() == loggedInUserId) {%>
                            <button class="icon-btn light"
                                    onclick="coverVue.openImgDescription('<%= StringEscapeUtils.escapeEcmaScript(photo.getDescription())%>', <%= photo.getID() %>, <%=i%>)"><i
                                    class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                            <%}%>
                        </span>
                    </div>
                </div>
                <%
                        }
                        i++;
                    }
                %>
            </div>
            <div class="edit-img-popup" v-show="popupImgShow">
                <input v-model="popupImg.description" spellcheck="false">
                <button class="icon-btn light" @click="submitImgDescription"><i class="fa fa-check"
                                                                                aria-hidden="true"></i>
                </button>
            </div>
            <% if (defaultModel.getCreator().getId() == loggedInUserId) {%>
            <div v-if="!uploadingCover" class="icon-btn light cover-image-button" @click="chooseCover()">
                <i class="fa fa-picture-o" aria-hidden="true"></i>
            </div>
            <%}%>
            <form action="" onsubmit="return false;" id="form-cover"></form>
        </div>

        <script id="slider-display" type="text/x-handlebars-template">
            {{#each data}}
            <div class="slider-block" style="background-image: url({{src}})">
                <%--<div class="caption">--%>
                <%--{{description}}--%>
                <%--<% if (defaultModel.getCreator().getId() == loggedInUserId) {%>--%>
                <%--<button class="edit-button edit-cover-button"--%>
                <%--onclick="coverVue.openImgDescription('{{escape description}}', {{ID}}, {{@index}})"><i--%>
                <%--class="fa fa-pencil-square-o" aria-hidden="true"></i></button>--%>
                <%--<%}%>--%>
                <%--</div>--%>
                <div class="caption">
                        <span>
                            {{description}}
                            <% if (defaultModel.getCreator().getId() == loggedInUserId) {%>
                            <button class="icon-btn light"
                                    onclick="coverVue.openImgDescription('{{escape description}}', {{ID}}, {{@index}})"><i
                                    class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                            <%}%>
                        </span>
                </div>
            </div>
            {{/each}}
        </script>

        <script>

            Handlebars.registerHelper('escape', function (text) {
                return text.replace("'", "\\'");
            });

            var slickIt = function () {
                $(".slick").slick({
                    dots: false,
                    autoplay: true,
                    autoplaySpeed: 7000
                });
            };

            $(document).ready(slickIt);
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
                    uploadingCover: false,
                    popupImg: {
                        description: "",
                        ID: "",
                        index: -1
                    },
                    popupImgShow: false,
                    updatingDescription: true
                },
                created: function () {
                },
                methods: {
                    openImgDescription: function (description, photoID, index) {
                        this.popupImg.description = description;
                        this.popupImg.ID = photoID;
                        this.popupImg.index = index;
                        this.popupImgShow = true;
                        this.updatingDescription = false;
                    },
                    submitImgDescription: function () {
                        if (this.updatingDescription) {
                            return;
                        }
                        this.updatingDescription = true;
                        var self = this;
                        axios({
                            url: "/UpdateImg?imgID=" + self.popupImg.ID,
                            method: "post",
                            params: {description: self.popupImg.description, hikeID: hikeId}
                        }).then(function (response) {
                            self.updateSlick(response);
                            $(".slick").slick("slickGoTo", self.popupImg.index);
                            self.popupImg.description = "";
                            self.popupImg.ID = "";
                            self.popupImg.index = "";
                            self.popupImgShow = false;
                        });
                    },
                    chooseCover: function () {
                        document.querySelector("#form-cover").innerHTML = "";
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
                            self.saveCover();
                        }
                    },

                    updateSlick: function (slides) {
                        var source = $("#slider-display").html();
                        var template = Handlebars.compile(source);
                        var slick = $(".slick");
                        slick.slick("unslick");
                        slick.html(template(slides));
                        slickIt();
                    },

                    saveCover: function () {
                        var th = this;
                        axios.post('/UploadCover?hikeID=' + <%= hikeId %>, new FormData(document.querySelector("#form-cover"))).then(function (response) {
                            th.updateSlick(response);
                            th.uploadingCover = false;
                            $('.slick').slick("slickGoTo", $(".slick").slick("getSlick").slideCount - 1);
                        });
                    },

                    removeCover: function () {
                        var th = this;
                        th.uploadingCover = false;
                    }

                }

            });

        </script>

        <div id="vueapp">
            <jsp:include page='<%= fullSubPage %>'/>

    </main>

</div>
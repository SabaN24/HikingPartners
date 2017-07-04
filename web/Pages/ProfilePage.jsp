<%@ page import="Models.User" %><%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 26-Jun-17
  Time: 06:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="../Content/css/profile.css">
<setTitle>
    Profile
</setTitle>

<div class="profile-content" id="vueapp">
    <div class="profile-cover" v-bind:style="{ backgroundImage: 'url(' + profileUser.coverPictureAddress + ')' }">
        <div class="profile-name-block">{{ profileUser.firstName }} {{ profileUser.lastName }} </div>
    </div>
    <div class="user-info-block">
        <div class="profile-picture" v-bind:style="{ backgroundImage: 'url(' + profileUser.profilePictureAddress + ')' }">

        </div>
        <div class="user-info">
            <div style="font-size: 18px; margin-bottom: 10px">ABOUT</div>
            <div class="profile-status">{{ profileUser.aboutMe }}</div>
            <a :href="profileUser.facebookLink" target="_blank" class="facebook-page-btn mybtn">
                Go To Facebook Profile
            </a>
            <button v-if="profileUser.id != user.id" @click="openConversation(profileUser.id)" class="facebook-page-btn mybtn">
                <i class="fa fa-commenting" aria-hidden="true"></i> Message
            </button>
            <div class="info-item">Gender: {{ profileUser.gender }}</div>
            <div class="info-item">Birth date: {{ !profileUser.birthDate ? "Hidden" : profileUser.birthDate }}</div>
            <div class="info-item">Email: {{ profileUser.email }}</div>
        </div>
    </div>
    <div class="created-hikes-block">
        <div class="created-hikes-block-inner">
            <div class="profile-title">Created Hikes</div>
            <ul class="created-hikes-list">
                <li class="created-hike bg" v-for="hike in createdHikes" @click="window.location = '/HikePage/Home?hikeId=' + hike.id" :style="{ backgroundImage: 'url(' + '../Content/img/pic1.jpg' + ')' }">
                    {{ hike.name }}
                </li>
            </ul>
        </div>
    </div>
</div>
<script src="../Scripts/axios.min.js"></script>
<script src="../Scripts/vue.min.js"></script>
<script>

    var app = new Vue({
        el: '#vueapp',
        data: {
            profileUser: {},
            createdHikes: []
        },
        created: function(){
            this.getProfileUser();
            this.getCreatedHikes();

        },
        methods: {
            getProfileUser: function(){
                var th = this;
                var userID = '<%= request.getParameter("userID") %>';
//                axios.post("/GetProfileUser?userID=" + userID,  {}).then(function(response){
//                    console.log(response);
//                    th.profileUser = response.data;
//                });
                axios({url: "/GetProfileUser", method: "post", params: {userID: userID}}).then(function (response) {
                    console.log(response);
                    th.profileUser = response.data;
                    if(!th.profileUser.coverPictureAddress)
                        th.profileUser.coverPictureAddress =  "https://unsplash.it/1920/1080?image=997";
                });


            },
            getCreatedHikes: function(){
                var th = this;
                axios.post("/CreatedHikesServlet?userID=<%= request.getParameter("userID") %>",  {}).then(function(response){
                    console.log(response);
                    th.createdHikes = response.data;
                });
            },
            openConversation: function (userId) {
                appChat.openChat(userId);
            }
        }
    });
</script>


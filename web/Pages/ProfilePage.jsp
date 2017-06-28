<%@ page import="Models.User" %><%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 26-Jun-17
  Time: 06:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="../Content/css/profile.css">
<link href="https://fonts.googleapis.com/css?family=Montserrat|Quicksand" rel="stylesheet">
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
            <a :href="profileUser.facebookLink" target="_blank" class="facebook-page-btn btn">
                Go To Facebook Profile
            </a>
            <div class="info-item">Gender: {{ profileUser.gender }}</div>
            <div class="info-item">Birth date: {{ !profileUser.birthDate ? "Hidden" : profileUser.birthDate }}</div>
            <div class="info-item">Email: {{ profileUser.email }}</div>
        </div>
    </div>
    <div class="created-hikes-block">
        <div class="created-hikes-block-inner">
            <div class="profile-title">Created Hikes</div>
            <ul class="created-hikes-list">
                <li class="created-hike" v-for="hike in createdHikes">
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
                axios.post("/GetProfileUser?userID=<%= request.getParameter("userID") %>",  {}).then(function(response){
                    console.log(response);
                    th.profileUser = response.data;
                });

            },
            getCreatedHikes: function(){
                var th = this;
                axios.post("/CreatedHikesServlet?userID=<%= request.getParameter("userID") %>",  {}).then(function(response){
                    console.log(response);
                    th.createdHikes = response.data;
                });
            }
        }
    });
</script>


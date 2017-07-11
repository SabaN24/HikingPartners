<%@ page import="Models.Notification" %><%--
  Created by IntelliJ IDEA.
  User: Saba
  Date: 7/4/2017
  Time: 3:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<div id="vueapp">

<div class="main-content notifications-container">
    <ul class="notifications-list__page">
        <li class="notifications-item__page" v-for="(notification, index) in notifications" v-if="notification.typeID == <%= Notification.REQUEST %>" :class="{notSeen : !notification.seen}">
            <div class="avatar-block" @mouseenter="hoverUser(notification.fromUser, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  notification.fromUser.id"
                 :style="{ backgroundImage: 'url(' + notification.fromUser.profilePictureAddress + ')' }"></div>
            <div class="notification-text">
                <span  class="notification-user-name"
                       @mouseenter="hoverUser(notification.fromUser, event)" @mouseleave="hoverOutUser(event)"
                       @click="window.location = '/Profile?userID=' +  notification.fromUser.id">
                    {{notification.fromUser.firstName}} {{notification.fromUser.lastName}}</span> wants to join
                <span class="notification-hike-name" @click="window.location = '/HikePage/Home?hikeId=' +  notification.hikeID">{{notification.hikeName}}</span>
            </div>
            <div class="request-btns">
                <button class="mybtn accept" @click="respondToRequest(request.id, 'accept', index)" onsubmit="return false;"> Accept </button>
                <button class="mybtn reject"  @click="respondToRequest(request.id, 'reject', index)" onsubmit="return false;"> Reject </button>
            </div>
        </li>
        <li class="notifications-item__page" v-for="(notification, index) in notifications" v-if="notification.typeID == <%= Notification.COMMENT %>"
            @click="window.location = '/HikePage/Feed?hikeId=' + notification.hikeID + '#' + notification.postID" :class="{notSeen : !notification.seen}">
            <div class="avatar-block" @mouseenter="hoverUser(notification.fromUser, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  notification.fromUser.id"
                 :style="{ backgroundImage: 'url(' + notification.fromUser.profilePictureAddress + ')' }"></div>
            <div class="notification-text">
                <span  class="notification-user-name"
                       @mouseenter="hoverUser(notification.fromUser, event)" @mouseleave="hoverOutUser(event)"
                       @click="window.location = '/Profile?userID=' +  notification.fromUser.id">
                    {{notification.fromUser.firstName}} {{notification.fromUser.lastName}}</span> commented in the post you are following.
            </div>
        </li>
        <li class="notifications-item__page" v-for="(notification, index) in notifications" v-if="notification.typeID == <%= Notification.LIKE %>"
            @click="window.location = '/HikePage/Feed?hikeId=' + notification.hikeID + '#' + notification.postID" :class="{notSeen : !notification.seen}">
            <div class="avatar-block" @mouseenter="hoverUser(notification.fromUser, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  notification.fromUser.id"
                 :style="{ backgroundImage: 'url(' + notification.fromUser.profilePictureAddress + ')' }"></div>
            <div class="notification-text">
                <span  class="notification-user-name"
                       @mouseenter="hoverUser(notification.fromUser, event)" @mouseleave="hoverOutUser(event)"
                       @click="window.location = '/Profile?userID=' +  notification.fromUser.id">
                    {{notification.fromUser.firstName}} {{notification.fromUser.lastName}}</span> liked your comment.
            </div>
        </li>
    </ul>
</div>

</div>

<script src="../Scripts/axios.min.js"></script>
<script src="../Scripts/vue.min.js"></script>


<script>

    Vue.filter('cutTime', function (value) {
        if (!value) return "";
        return value.substr(0, value.length - 6);
    });

    var app = new Vue({
        el: '#vueapp',
        data: {
            notifications: []
        },

        created: function () {
            var th = this;
            axios.post("/GetNotifications", {}).then(function(response){
                th.notifications = response.data;
            });
        },

        methods: {

            respondToRequest: function(requestId, response, index) {
                axios({url: "/RespondToRequest", method:"post", params:{requestId: requestId, response: response}});
                this.requests.splice(index, 1);
            },
            hoverUser: function(user, e){
                if(this.profPopupActive) return;
                this.hoveredUser = user;
                this.profPopupActive = true;
                var popup = document.getElementsByClassName('profile-popup-wrapper')[0];
                var rect = e.target.getBoundingClientRect();
                popup.style.left = rect.left + pageXOffset +'px';
                popup.style.top = rect.top + pageYOffset + e.target.clientHeight - 5 +'px';
            },
            hoverOutUser: function (e) {
                if(!this.profPopupActive) return;
                if(document.querySelectorAll(".profile-popup-wrapper:hover").length) return;
                this.profPopupActive = false;

            }

        }
    });

</script>
</html>

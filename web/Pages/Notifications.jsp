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
        <li class="notifications-item__page" v-for="(notification, index) in notifications" :class="{notSeen : !notification.seen}" @click="clickNotification(notification)">
            <div class="avatar-block"
                 @click.stop="window.location = '/Profile?userID=' +  notification.fromUser.id"
                 :style="{ backgroundImage: 'url(' + notification.fromUser.profilePictureAddress + ')' }"></div>
            <div class="notification-text">
                <div>
                    <span  class="notification-user-name" @click="window.location = '/Profile?userID=' +  notification.fromUser.id">
                        {{notification.fromUser.firstName}} {{notification.fromUser.lastName}}
                    </span>
                    <span> wants to join<span class="notification-hike-name" @click.stop="window.location = '/HikePage/Home?hikeId=' +  notification.hikeID"> {{notification.hikeName}}</span></span>
                    <span v-if="notification.typeID == <%= Notification.COMMENT %>" > commented on the post you are following.</span>
                    <span v-if="notification.typeID == <%= Notification.LIKE %>" > liked your comment.</span>
                </div>
                <div class="notification-time">{{notification.time | cutTime}}</div>
            </div>
            <div class="request-btns" v-if="notification.typeID == <%= Notification.REQUEST %>">
                <button class="mybtn accept" @click.stop="respondToRequest(notification.requestID, 'accept', index)"> Accept </button>
                <button class="mybtn reject"  @click.stop="respondToRequest(notification.requestID, 'reject', index)"> Reject </button>
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
                th.notifications = response.data.reverse();
            });
        },

        methods: {

            respondToRequest: function(requestId, response, index) {
                console.log(requestId + " " + response);
                axios({url: "/RespondToRequest", method:"post", params:{requestId: requestId, response: response}});
                this.notifications.splice(index, 1);
            },
            clickNotification: function(not){
                if(not.typeID != '<%= Notification.REQUEST %>')
                    window.location = '/HikePage/Feed?hikeId=' + not.hikeID + '#' + not.postID;

            }
        }
    });

</script>
</html>

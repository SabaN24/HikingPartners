<%--
  Created by IntelliJ IDEA.
  User: Saba
  Date: 7/4/2017
  Time: 3:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<div id="vueapp">

<div class="main-content request-container">
    <ul class="request-list">
        <li class="request-item" v-for="(request, index) in requests">
            <div class="avatar-block" @mouseenter="hoverUser(request.sender, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  request.sender.id"
                 :style="{ backgroundImage: 'url(' + request.sender.profilePictureAddress + ')' }"></div>
            <div class="request-text">
                <span  class="request-user-name"
                       @mouseenter="hoverUser(request.sender, event)" @mouseleave="hoverOutUser(event)"
                       @click="window.location = '/Profile?userID=' +  request.sender.id">
                    {{request.sender.firstName}} {{request.sender.lastName}}</span> wants to join
                <span class="request-hike-name" @click="window.location = '/HikePage/Home?hikeId=' +  request.hike.id">{{request.hike.name}}</span>
            </div>
            <div class="request-btns">
                <button class="mybtn accept" @click="respondToRequest(request.id, 'accept', index)" onsubmit="return false;"> Accept </button>
                <button class="mybtn reject"  @click="respondToRequest(request.id, 'reject', index)" onsubmit="return false;"> Reject </button>
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
            requests: {
                sender: "",
                receiver: "",
                hike: "",
                profPopupActive: false,
                hoveredUser: {}
            },
        },

        created: function () {
            var th = this;
            axios.post("/GetRequests", {}).then(function (response) {
                th.requests = response.data.reverse();
                console.log(th.requests);
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

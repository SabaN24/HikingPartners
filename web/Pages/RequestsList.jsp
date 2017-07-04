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
            <div class="avatar-block"></div>
            <div class="request-text">
                {{request.sender.firstName}} {{request.sender.lastName}} wants to join {{request.hike.name}}
            </div>
            <button class="accept-button" @click="respondToRequest(request.id, 'accept', index)" onsubmit="return false;"> Accept </button>
            <button class="reject-button" @click="respondToRequest(request.id, 'reject', index)" onsubmit="return false;"> Reject </button>
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
                hike: ""
            },
        },

        created: function () {
            var th = this;
            axios.post("/GetRequests", {}).then(function (response) {
                th.requests = response.data.reverse();
            });
        },

        methods: {

            respondToRequest: function(requestId, response, index) {
                axios({url: "/RespondToRequest", method:"post", params:{requestId: requestId, response: response}});
                this.requests.splice(index, 1);
            }

        }
    });

</script>
</html>

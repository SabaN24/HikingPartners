<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 01-Jul-17
  Time: 05:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="vueapp">
    <div class="main-content members-block">
        <div>Creator: {{creator.firstName}} {{creator.lastName}}</div>
        <br>
        <div>Members:</div>
        <div v-for="member in members">{{member.firstName}} {{member.lastName}}</div>
    </div>

</div>

<script>
    var app = new Vue({
        el: "#vueapp",
        data: {
            test: "sandro",
            members: [],
            creator: {}
        },
        created: function () {
            var th = this;
            axios({url: "/GetHikeMembers", method:"post", params:{hikeID: hikeId}}).then(function (response) {
                th.members = response.data;
                var creator = th.members.find(function(el){return el.roleID == 1});
                th.creator = creator;
                var idx = th.members.indexOf(creator);
                th.members.splice(idx, 1);
            });
        }
    });
</script>

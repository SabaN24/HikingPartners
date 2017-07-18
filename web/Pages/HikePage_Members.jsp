<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 01-Jul-17
  Time: 05:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="main-content members-block">
    <div class="members-block-inner">
        <div class="member-block"  v-for="member in members">
            <div class="avatar-block" @mouseenter="profilePopupVue.hoverUser(member, event)" @mouseleave="profilePopupVue.hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  member.id"
                 :style="{ backgroundImage: 'url(' + member.profilePictureAddress + ')' }"></div>
            <div class="member-name" @mouseenter="profilePopupVue.hoverUser(member, event)" @mouseleave="profilePopupVue.hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  member.id">
                {{member.firstName}} {{member.lastName}}
            </div>
        </div>
    </div>
</div>


<script>
    var app = new Vue({
        el: "#vueapp",
        data: {
            test: "sandro",
            members: [],
            creatorID: {},
            profilePopupVue: profilePopupVue
        },
        created: function () {
            var th = this;
            axios({url: "/GetHikeMembers", method:"post", params:{hikeID: hikeId}}).then(function (response) {
                th.members = response.data;
                var creator = th.members.find(function(el){return el.roleID == 1});
                th.creatorID = creator.ID;
            });
        },
        methods: {

            openConversation: function (userId) {
                appChat.openChat(userId);
            }

        }
    });
</script>

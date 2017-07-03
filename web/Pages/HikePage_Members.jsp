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
        <div class="title title-medium">
            Creator:
        </div>
        <div class="member-block creator">
            <div class="avatar-block" @mouseenter="hoverUser(creator, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  creator.id"
                 :style="{ backgroundImage: 'url(' + creator.profilePictureAddress + ')' }">
            </div>
            <div class="member-name" @mouseenter="hoverUser(creator, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  creator.id"
                 :class="{active : profPopupActive}">
                {{creator.firstName}} {{creator.lastName}}
            </div>
        </div>
        <div class="title title-medium">
            Members:
        </div>
        <div class="member-block"  v-for="member in members">
            <div class="avatar-block" @mouseenter="hoverUser(member, event)" @mouseleave="hoverOutUser(event)"
                 @click="window.location = '/Profile?userID=' +  member.id"
                 :style="{ backgroundImage: 'url(' + member.profilePictureAddress + ')' }"></div>
            <div class="member-name" @mouseenter="hoverUser(member, event)" @mouseleave="hoverOutUser(event)"
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
            creator: {},
            profPopupActive: false,
            hoveredUser: {}
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
        },
        methods: {
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

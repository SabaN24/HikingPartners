<%@ page import="Servlets.Helper" %>
<%@ page import="Models.User" %><%--
  Created by IntelliJ IDEA.
  User: Levani
  Date: 13.06.2017
  Time: 13:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title></title>
    <link rel="stylesheet" href="../Content/css/normalize.css">
    <link rel="stylesheet" href="../Content/css/font-awesome.min.css">
    <link rel="stylesheet" href="../Content/css/common.css">
    <link rel="stylesheet" href="../Content/css/main.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat|Quicksand" rel="stylesheet">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAN41T3N0B5Tx61omm8n9ZX6quK4FvG1jk" type="text/javascript"></script>
</head>
<body>
<%
    String pageName = (String)request.getAttribute("page");
    User loggedInUser = (User)request.getAttribute("loggedInUser");
%>
<% if(!pageName.equals("LoginPage.jsp")){ %>
<script>
    var user = {
        id : '<%= loggedInUser.getId() %>',
        firstName: '<%= loggedInUser.getFirstName()%>',
        lastName: '<%= loggedInUser.getLastName() %>',
        profilePictureAddress: '<%= loggedInUser.getProfilePictureAddress() %>'
    };

</script>
<% } %>
<div class="wrapper clearfix">
    <% if(!pageName.equals("LoginPage.jsp")){ %>
    <header>
        <div class="header-left">
            <div class="logo-block">
                <a class="logo-link" href="/Home">
                    <img src="../Content/img/logo.png" alt="" class="logo-img">
                </a>
            </div>
        </div>
        <div class="header-right">
            <div class="mynav">
                <div class="mynav-item"><a href="/RequestListServlet" class="mynav-link">Notifications</a></div>
            </div>
            <div class="profile-block">

                <a href="/Profile?userID=<%=loggedInUser.getId()%>" class="profile-link">
                    <div class="avatar-block" style="background-image: url(<%= loggedInUser.getProfilePictureAddress() %>) ">

                    </div>
                    <div class="hidden logged-user-id"><%=loggedInUser.getId()%></div>
                    <div class="profile-name">
                        <%=loggedInUser.getFirstName()%> <%=loggedInUser.getLastName()%>
                    </div>
                </a>
                <div class="logout-block"><a href="/Logout">Log out</a></div>
            </div>
        </div>

    </header>
    <% } %>


    <%--<% if(pageName.equals("LoginPage.jsp")){ %>--%>
    <jsp:include page='<%= pageName %>'/>
    <%--<script>document.querySelectorAll("title")[0].innerHTML = document.querySelectorAll("setTitle")[0].innerHTML;</script>--%>
    <%--<% } %>--%>


    <% if (!pageName.equals("LoginPage.jsp")) { %>

    <div id="chatVue">
        <div class="chats">
            <div class="chat" v-for="(chat, index) in chats">
                <div class="chat-upper">
                    <div class="chat-user-name" @click="window.location = '/Profile?userID=' + chat.userTo.id">{{chat.userTo.firstName}} {{chat.userTo.lastName}}</div>
                    <div class="chat-close-btn" @click="closeChat(chat.toUserId)"><i class="fa fa-minus" aria-hidden="true"></i></div>
                </div>
                <div class="messages-block">
                    <ul class="messages-list">
                        <li v-for="(message, index) in chat.messages" class="message">
                            <div class="avatar-block" @click="window.location = '/Profile?userID=' + message.userFrom.id" v-if="loggedInUser.id != message.userFrom.id" :style="{ backgroundImage: 'url(' + message.userFrom.profilePictureAddress + ')' }"></div>
                            <div class="message-text-wrapper" :class="{ 'self-message' : loggedInUser.id == message.userFrom.id }">
                                <div class="message-text" >
                                    {{message.message}}
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="chat-add-message">
                    <form action="HikePageServlet" @submit.prevent="sendMessage(chat.toUserId, index)" method="post">
                        <input class="new-message-input" v-model="chat.newMessageInput" type="text" autocomplete="off"
                               name="add-message"
                                  placeholder="Send Message...">
                    </form>
                </div>
            </div>
        </div>
    </div>
    <% } %>


</div>
<footer>
    <div class="footer-info"> All Rights Reserved  © HikingPartners.ge  2017</div>
</footer>


<% if (!pageName.equals("LoginPage.jsp")) { %>

<script>
    Vue.filter('cutTime', function (value) {
        return value.substr(0, value.length - 6);
    });

    var mws = new WebSocket("ws://localhost:8080/MessagesSocket/<%= loggedInUser.getId() %>");

    var appChat = new Vue({
        el: '#chatVue',
        //These are stored instance variables for vue,
        //it will use these to bind element data ands
        //modify them.
        data: {
            chats: [],
            newMessage: "",
            loggedInUser: user

        },
        //These functions will be called when page loads.
        created: function () {
            this.fetchChats();
        },
        updated: function () {
        },
        //These are stored methods that vue will be able to use.
        methods: {

            fetchChats: function () {
                var th = this;
                axios.post("/OpenChatsServlet?userId=" + <%= loggedInUser.getId() %>, {}).then(function(response){
                    th.chats = response.data.reverse();
                    th.chats.forEach(function (elem) {
                       elem.newMessageInput = "";
                    });
                    th.updateChatScroll();
                });
            },

            fetchChat: function(toUserId){
                var th = this;
                axios.post("/OpenChatServlet?toUserId=" + toUserId, {}).then(function(response){
                    var chat = response.data;
                    chat.newMessageInput = "";
                    th.chats.unshift(chat);
                    th.updateChatScroll();
                });
            },

            //This method is invoked automatically when socket
            //server sends message to this session.
            getSocketMessage: function (data) {
                var jsonData = JSON.parse(data.data);
                var action = jsonData.action;
                data = jsonData.data;
                var th = this;
                if (action === "getMessage") {
                    var idx = th.chats.findIndex(x => x.toUserId == data.userFrom.id);
                    if (idx == -1) {
                        this.fetchChat(data.userFrom.id);
                    }
                    th.chats.find(x => x.toUserId == data.userFrom.id).messages.push(data);
                }
                if (action === "sendMessage") {
                    var idx = th.chats.map(function(e) { return e.toUserId; }).indexOf(data.userTo.id);
                    th.chats[idx].messages.push(data);
                    th.updateChatScroll(idx);
                }
                if(action === "openChat"){
                    var idx = th.chats.findIndex(x => x.toUserId === data.id);
                    if (idx == -1) {
                        this.fetchChat(data.id);
                    }
                }
            },

            openChat: function (toUserId) {

                mws.send(JSON.stringify({
                    action: "openChat",
                    toUserId: toUserId + "",
                }));

            },

            closeChat: function (toUserId){

                //delete from chats in vue app
                var th = this;
                var idx = th.chats.findIndex(x => x.toUserId === toUserId);
                th.chats.splice(idx, 1);

                //delete from database;
                axios.post("/CloseChatServlet?toUserId=" +  toUserId, {}).then(function () {
                    th.updateChatScroll();
                });
            },

            sendMessage: function (toUserId) {
                var msg = this.chats.find(function(chat){return chat.toUserId == toUserId}).newMessageInput;
                if(!msg) return;
                mws.send(JSON.stringify({
                    action: "getMessage",
                    data: {
                        toUserId: toUserId + "",
                        message: msg + "",
                    }
                }));
                this.chats.find(function(chat){return chat.toUserId == toUserId}).newMessageInput = "";
            },

            updateChatScroll : function (idx) {
                setTimeout(function () {
                    if(idx !== undefined){
                        var elem = document.getElementsByClassName("messages-block")[idx];
                        elem.scrollTop = elem.scrollHeight;
                    }else {
                        var elements = document.getElementsByClassName("messages-block");
                        Array.prototype.forEach.call(elements, function(elem) {
                            elem.scrollTop = elem.scrollHeight;
                        });
                    }
                }, 0);

            },
//            textAreaAdjust : function(e, idx) {
//                var o = e.target;
//                o.style.height = "1px";
//                o.style.height = (o.scrollHeight)+"px";
//                var el = document.querySelectorAll(".messages-block")[idx];
//                el.style.height = 274 - o.scrollHeight + 'px';
//
//
//            }
        }




    });

    mws.onmessage = appChat.getSocketMessage;


</script>


<% } %>

</body>
</html>



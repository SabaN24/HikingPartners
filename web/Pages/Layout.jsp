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

        <div class="chats-block" v-for="(chat, index) in chats">
            <div class="messages-block">
                <div class="chat-header" style="color: red" >
                    <span> <b> {{chat.userTo.firstName}} </b> </span> <span> <b> {{chat.userTo.lastName}} </b> </span>
                </div>
                <button  v-on:click="closeChat(chat.toUserId)"> close </button>


                <div class="messages-block-inner">
                    <ul class="messages-list"
                        v-for="(message, index) in chat.messages"
                    >
                        <li class="message">
                            <div class="avatar-block"
                                 v-bind:style="{ backgroundImage: 'url(' + message.userFrom.profilePictureAddress + ')' }"></div>
                            <div class="message-text" style="color: cyan">
                                {{message.message}}
                            </div>
                        </li>
                    </ul>


                    <div class="message">

                        <form action="HikePageServlet" v-on:submit.prevent="sendMessage(chat.toUserId)" method="post">
                            <input v-model="newMessage" class="message-input" type="text" autocomplete="off"
                                   name="add-message"
                                   placeholder="Send Message...">
                        </form>

                    </div>


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
        return value.substr(0, value.length - 3);
    });

    var mws = new WebSocket("ws://localhost:8080/MessagesSocket/<%= loggedInUser.getId() %>");

    var appChat = new Vue({
        el: '#chatVue',
        //These are stored instance variables for vue,
        //it will use these to bind element data ands
        //modify them.
        data: {
            chats: [],
            newMessage: ""

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
                    th.chats = response.data;
                });
            },

            fetchChat: function(toUserId){
                var th = this;
                axios.post("/OpenChatServlet?toUserId=" + toUserId, {}).then(function(response){
                    //console.log(response.data);
                    th.chats.push(response.data);
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

                    console.log("getshia");

                }


                if (action === "sendMessage") {

                    th.chats.find(x => x.toUserId == data.userTo.id).messages.push(data);

                    console.log("sendshia");

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
                axios.post("/CloseChatServlet?toUserId=" +  toUserId, {});
            },

            sendMessage: function (toUserId) {

                mws.send(JSON.stringify({
                    action: "getMessage",
                    data: {
                        toUserId: toUserId + "",
                        message: this.newMessage + "",
                    }
                }));
                this.newMessage = "";

            }


        }


    });

    mws.onmessage = appChat.getSocketMessage;


</script>


<% } %>

</body>
</html>



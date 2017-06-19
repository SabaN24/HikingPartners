<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 18-Jun-17
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>


<div class="hike_home" id="home">
    <div class="description-block">
        <div class="description-wrapper">
            <div class="description-header">
                <div class="description-header__left"> {{aboutModel.name}}</div>
                <div class="description-header__right">
                            <span title="ადამიანების მაქსიმალური რაოდენობა" style="margin-right: 30px;"><i
                                    class="fa fa-user" aria-hidden="true"></i> {{aboutModel.maxPeople}}</span>
                    <span title="გამგზავრების თარიღი" style="margin-right: 20px;"><i class="fa fa-arrow-up"
                                                                                     aria-hidden="true"></i> {{aboutModel.startDate}}</span>
                    <span title="ჩამოსვლის თარიღი"><i class="fa fa-arrow-down" aria-hidden="true"></i> {{aboutModel.endDate}}</span>

                </div>
            </div>
            <div class="description-body">
                <div style="margin-bottom: 10px;font-weight:bold;">Description:</div>
                {{aboutModel.description}}
            </div>
        </div>
        <div class="comments-count">{{aboutModel.comments.length}} comments.</div>
        <div class="comments-block">
            <div class="comments-block-inner">
                <ul class="comments-list" v-for="(comment, index) in aboutModel.comments">
                    <li class="comment">
                        <div class="avatar-block"></div>
                        <div class="comment-info">
                            <div class="comment-info__upper">
                                <div class="comment-author">
                                    <span>{{comment.user.firstName}} </span><span>{{comment.user.lastName}}</span>
                                </div>
                            </div>
                            <div class="comment-info__lower">
                                <div class="comment-time">{{comment.date}}
                                </div>
                                <div class="like-block">
                                    <i class="fa fa-thumbs-up" v-bind:class="{ liked: comment.isLiked }"
                                       v-on:click="like(index)" aria-hidden="true"></i>
                                    {{comment.likeNumber}}
                                </div>
                            </div>
                        </div>
                        <div class="comment-text">{{comment.comment}}
                        </div>
                    </li>
                </ul>
                <div class="comment">
                    <div class="add-comment">
                        <div class="avatar-block">
                        </div>
                        <form action="HikePageServlet" v-on:submit.prevent="sendComment" method="post">
                            <input class="comment-input" type="text" name="add-comment"
                                   placeholder="Write a comment...">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    Vue.filter('reverse', function (value) {
        return value.slice().reverse();
    });

    var hikeId = <%=request.getParameter("hikeId") == null ? 1 : request.getParameter("hikeId")%>;
    var ws = new WebSocket("ws://localhost:8080/HikeCommentsSocket/" + hikeId);
    var app = new Vue({
        el: '#vueapp',

        //These are stored instance variables for vue,
        //it will use these to bind element data and
        //modify them.
        data: {
            aboutModel: {}
        },
        //These functions will be called when page loads.
        created: function () {
            this.fetchData()
        },
        updated: function () {
            document.getElementsByTagName("title")[0].innerHTML = document.getElementsByTagName("setTitle")[0].innerHTML;
        },
        //These are stored methods that vue will be able to use.
        methods: {
            formatDate: function (date) {
                var months = [
                    "Jan", "Feb", "Mar",
                    "Apr", "May", "Jun", "Jul",
                    "Aug", "Sep", "Oct",
                    "Nov", "Dec"
                ];

                var day = date.getDate();
                var month = date.getMonth();
                var year = date.getFullYear();
                var hours = date.getHours();
                var minutes = date.getMinutes();

                return months[month] + ', ' + day + ", " + year + " " + hours + ":" + minutes;
            },

            fetchData: function () {
                var xhr = new XMLHttpRequest();
                var self = this;
                xhr.open('POST', "/HikeCommentsServlet?hikeId=" + hikeId);
                xhr.onload = function () {
                    self.aboutModel = JSON.parse(xhr.responseText);
                };
                xhr.send();
            },

            //This method is invoked automatically when socket
            //server sends messageto this session.
            getSocketMessage: function (data) {
                var jsonData = JSON.parse(data.data);
                var action = jsonData.action;
                data = jsonData.data;
                if (action == "getComment") {
                    this.aboutModel.comments.push(data);
                } else if (action == "getCommentLike") {
                    if (data.likeResult == "like") {
                        this.aboutModel.comments[data.commentIndex].likeNumber++;
                        if (data.userID == 1) {
                            this.aboutModel.comments[data.commentIndex].isLiked = true;
                        }
                    } else if (data.likeResult == "unlike" && this.aboutModel.comments[data.commentIndex].likeNumber > 0) {
                        this.aboutModel.comments[data.commentIndex].likeNumber--;
                        if (data.userID == 1) {
                            this.aboutModel.comments[data.commentIndex].isLiked = false;
                        }
                    }
                }
            },

            //Sends new comment to socket server, called when enter is hit on comment.
            sendComment: function () {
                ws.send(JSON.stringify({
                    action: "getComment",
                    data: {
                        comment: document.getElementsByClassName("comment-input")[0].value,
                        commentID: "" + 0,
                        date: this.formatDate(new Date()),
                        likeNumber: "" + 0,
                        isLiked: false,
                        userID: "" + 1,
                        user: {
                            firstName: document.getElementsByClassName("profile-name")[0].innerHTML.trim().split(" ")[0],
                            lastName: document.getElementsByClassName("profile-name")[0].innerHTML.trim().split(" ")[1]
                        }
                    }
                }));
                document.getElementsByClassName("comment-input")[0].value = '';
            },

            //This function is called when like button is clicked.
            like: function (index) {
                ws.send(JSON.stringify({
                    action: "getCommentLike",
                    data: {
                        commentID: "" + this.aboutModel.comments[index].commentID,
                        userID: "" + 1,
                        commentIndex: "" + index
                    }
                }));
            }
        }
    });
    ws.onmessage = app.getSocketMessage;
</script>

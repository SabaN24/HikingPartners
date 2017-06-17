<%@ page import="Models.Comment" %>
<%@ page import="Models.Hike.AboutModel" %>
<%@ page import="Models.Hike.DefaultModel" %>
<%@ page import="Models.MiniUser" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 12-Jun-17
  Time: 01:18
  To change this template use File | Settings | File Templates.
--%>
<div id="vueapp">
<setTitle>
    {{aboutModel.name}}
</setTitle>

</header>
<aside>
    <div class="creator-block">
        <div class="avatar-block">

        </div>
        <div class="name-block">
            <div class="name-text">
                <%
                    DefaultModel defaultModel = (DefaultModel)request.getAttribute(DefaultModel.ATTR);
                    MiniUser creator = defaultModel.getCreator();
                    out.print(creator.getFirstName() + " " + creator.getLastName());
                %>
            </div>
            <div class="role-block">Creator</div>
        </div>
    </div>
    <nav>
        <ul class="nav-list">
            <li class="nav-item active"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Hike Page</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Items</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Members</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Gallery</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Locations</a></li>
            <li class="nav-item"><a href="#" class="nav-link"><i class="fa fa-bicycle"></i> Feed</a></li>
        </ul>
    </nav>
</aside>
<main>
    <div class="main-content">
        <div class="slider-block">
            <div class="caption">
                <%=  defaultModel.getCoverPhotos().get(0).getLocationName() %>
            </div>
        </div>
        <div class="description-block">
            <div class="description-wrapper">
                <div class="description-header">
                    <div class="description-header__left"> {{aboutModel.name}} </div>
                    <div class="description-header__right">
                        <span title="ადამიანების მაქსიმალური რაოდენობა" style="margin-right: 30px;"><i class="fa fa-user" aria-hidden="true"></i> {{aboutModel.maxPeople}}</span>
                        <span title="გამგზავრების თარიღი" style="margin-right: 20px;"><i class="fa fa-arrow-up" aria-hidden="true"></i> {{aboutModel.startDate}}</span>
                        <span title="ჩამოსვლის თარიღი"><i class="fa fa-arrow-down" aria-hidden="true"></i> {{aboutModel.endDate}}</span>

                    </div>
                    <%--out.print(" <div class=\"description-header__right\">" + aboutModel.getMaxPeople() + "</div>");--%>
                </div>
                <div class="description-body">
                    <div style="margin-bottom: 10px;font-weight:bold;">Description:</div>
                    {{aboutModel.description}}
                </div>
            </div>
            <div class="comments-count">{{aboutModel.comments.length}} comments.</div>
            <div class="comments-block">
                <div class="comments-block-inner">
                    <div class="comment">
                        <div class="add-comment">
                            <div class="avatar-block">
                            </div>
                            <form action="HikePageServlet" v-on:submit.prevent="sendComment" method="post">
                                <input class="comment-input" type="text" name="add-comment" placeholder="Write a comment...">
                            </form>
                        </div>
                    </div>
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
                                        <i class="fa fa-thumbs-up" v-bind:class="{ liked: comment.isLiked }" v-on:click="like(index)" aria-hidden="true"></i>
                                        {{comment.likeNumber}}
                                    </div>
                                </div>
                            </div>
                            <div class="comment-text">{{comment.comment}}
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</main>
</div>
<script>
    Vue.filter('reverse', function(value) {
        return value.slice().reverse();
    });
    var ws = new WebSocket("ws://localhost:8080/HikeCommentsSocket/1");
    var newComment = null;
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
        updated: function() {
            document.getElementsByTagName("title")[0].innerHTML = document.getElementsByTagName("setTitle")[0].innerHTML;
        },
        //These are stored methods that vue will be able to use.
        methods: {
            fetchData: function () {
                var xhr = new XMLHttpRequest();
                var self = this;
                xhr.open('POST', "/HikeCommentsServlet");
                xhr.onload = function () {
                    self.aboutModel = JSON.parse(xhr.responseText);
                };
                xhr.send();
            },

            //This method is invoked automatically when socket
            //server sends messageto this session.
            getSocketMessage: function(data){
                var jsonData = JSON.parse(data.data);
                var action = jsonData.action;
                var comment = jsonData.data;
                if(action == "getComment") {
                    this.aboutModel.comments.splice(0, 0, comment);
                }
            },

            //Sends new comment to socket server, called when enter is hit on comment.
            sendComment: function(){
                ws.send(JSON.stringify({
                    action: "getComment",
                    data: {
                        comment: document.getElementsByClassName("comment-input")[0].value,
                        commentID: 0,
                        date: Date.now(),
                        likeNumber: 0,
                        isLiked: false,
                        user: {
                            firstName: document.getElementsByClassName("profile-name")[0].innerHTML.trim().split(" ")[0],
                            lastName: document.getElementsByClassName("profile-name")[0].innerHTML.trim().split(" ")[1]
                        }
                    }
                }));
                document.getElementsByClassName("comment-input")[0].value = '';
            },

            //This function is called when like button is clicked.
            like: function(index){
                //Here we should send new like/unlike to socket server
                //using ws.send(message) where message is json transformed to string
                //and contains message.action = 'getLike' and message.newLike, which has
                //two values, comment id and user id.

                //These will be called only after socket sends back information about like
                //if it was liked, disliked, by whom it was done and if there was error on server.
                this.aboutModel.comments[index].isLiked = !this.aboutModel.comments[index].isLiked;
                this.aboutModel.comments[index].isLiked ? this.aboutModel.comments[index].likeNumber++ : this.aboutModel.comments[index].likeNumber--;

            }
        }
    });
    ws.onmessage = app.getSocketMessage;
</script>
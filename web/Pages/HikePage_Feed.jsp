<%--
  Created by IntelliJ IDEA.
  User: Nodo
  Date: 6/19/2017
  Time: 9:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="hike_feed ">
    <div class="post-block main-content">
        <div class = "new-post">
            <div class="avatar-block post-author-avatar" v-bind:style="{ backgroundImage: 'url(' + user.profilePictureAddress + ')' }"></div>
            <form action="" class="post-form" v-on:submit.prevent="sendPost">
                <input type="text" placeholder="Write something..." class="new-post-input" autocomplete="off" v-model="newPostText">
            </form>
        </div>
    </div>
    <div class="post-block main-content" v-for="(post, index) in posts" >
        <div class = "post-upper">
            <div @click="window.location = '/Profile?userID=' +  post.user.id" class = "avatar-block post-author-avatar" v-bind:style="{ backgroundImage: 'url(' + post.user.profilePictureAddress + ')' }"></div>
            <div class="post-info">
                    <div class="post-author-name comment-author" @click="window.location = '/Profile?userID=' +  post.user.id">
                        <span>{{post.user.firstName}} </span><span>{{post.user.lastName}}</span>

                    </div>
                    <div class="post-time">
                        {{post.time | cutTime}}
                    </div>
            </div>
        </div>

        <div class = "post-text">
            {{post.text}}
        </div>
        <div class="comments-count">
            {{post.comments.length}} comment<span v-show="post.comments.length != 1">s</span>
        </div>
        <div class="comments-block">
            <div class="comments-block-inner">
                <ul class="comments-list">
                    <li class="comment" v-for="(comment, index) in post.comments">
                        <div @click="window.location = '/Profile?userID=' +  comment.user.id" class="avatar-block" v-bind:style="{ backgroundImage: 'url(' + comment.user.profilePictureAddress + ')' }"></div>
                        <div class="comment-info">
                            <div class="comment-info__upper">
                                <div @click="window.location = '/Profile?userID=' +  comment.user.id" class="comment-author">
                                    <span>{{comment.user.firstName}} </span><span>{{comment.user.lastName}}</span>
                                </div>
                            </div>
                            <div class="comment-info__lower">
                                <div class="comment-time">{{comment.date | cutTime}}
                                </div>
                                <div class="like-block">
                                    <i class="fa fa-thumbs-up" v-bind:class="{ liked: comment.isLiked }"
                                       v-on:click="like(post.id, comment.commentID)" aria-hidden="true"></i>
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
                        <div class="avatar-block"  v-bind:style="{ backgroundImage: 'url(' + user.profilePictureAddress + ')' }">
                        </div>
                        <form action="HikePageServlet" v-on:submit.prevent="sendComment(post.id)" method="post">
                            <input v-model="commentInputs[post.id]" class="comment-input" type="text" autocomplete="off" name="add-comment"
                                   placeholder="Write a comment...">
                        </form>
                    </div>
                </div>
            </div>
        </div>



    </div>
</div>

<script>
    Vue.filter('cutTime', function (value) {
        return value.substr(0, value.length - 3);
    });

    var ws = new WebSocket("ws://localhost:8080/HikeFeedSocket/" + hikeId);
    var app = new Vue({
        el: '#vueapp',

        //These are stored instance variables for vue,
        //it will use these to bind element data and
        //modify them.
        data: {
            posts: [],
            commentInputs: {},
            newPostText: "",
            user: {}
        },
        //These functions will be called when page loads.
        created: function () {
            this.fetchData();
            this.user = user;
            console.log(this.user);
        },
        updated: function () {
            document.getElementsByTagName("title")[0].innerHTML = document.getElementsByTagName("setTitle")[0].innerHTML;
        },
        //These are stored methods that vue will be able to use.
        methods: {
            fetchData: function () {
                var th = this;
                axios.post("/HikePostPageServlet?hikeId=" + hikeId, {}).then(function(response){
                    th.posts = response.data.reverse();
                });
            },

            //This method is invoked automatically when socket
            //server sends messageto this session.
            getSocketMessage: function (data) {
                console.log("----------");
                console.log(data);
                var jsonData = JSON.parse(data.data);
                console.log(jsonData);
                var action = jsonData.action;
                data = jsonData.data;
                if (action == "getComment") {
                    this.posts.find(x => x.id == data.postID).comments.push(data);
                } else if (action == "getCommentLike") {
                    if (data.liked) {
                        this.posts.find(x => x.id == data.postID).comments.find(x => x.commentID == data.commentID).likeNumber++;
                        if (data.userID == user.id) {
                            this.posts.find(x => x.id == data.postID).comments.find(x => x.commentID == data.commentID).isLiked = true;
                        }
                    } else if (this.posts.find(x => x.id == data.postID).comments.find(x => x.commentID == data.commentID).likeNumber > 0) {
                        this.posts.find(x => x.id == data.postID).comments.find(x => x.commentID == data.commentID).likeNumber--;
                        if (data.userID == user.id) {
                            this.posts.find(x => x.id == data.postID).comments.find(x => x.commentID == data.commentID).isLiked = false;
                        }
                    }
                }else if(action == "getPost"){
                    this.posts.unshift(data);
                }
            },

            //Sends new comment to socket server, called when enter is hit on comment.
            sendComment: function (postId) {
                var newCommentText = this.commentInputs[postId];
                ws.send(JSON.stringify({
                    action: "getComment",
                    data: {
                        comment: newCommentText + "",
                        postID: postId + ""
                    }
                }));
                this.commentInputs[postId] = "";

            },

            sendPost: function () {
                ws.send(JSON.stringify({
                    action: "getPost",
                    data: {
                        post: this.newPostText + "",
                    }
                }));
                this.newPostText = "";

            },



            //This function is called when like button is clicked.
            like: function (postID, commentID) {
                ws.send(JSON.stringify({
                    action: "getCommentLike",
                    data: {
                        postID: "" + postID,
                        commentID: "" + commentID,
                    }
                }));
            }
        }
    });
    ws.onmessage = app.getSocketMessage;
</script>

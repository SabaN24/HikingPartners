<%@ page import="Models.Hike.DefaultModel" %>
<%@ page import="Database.HikeDM" %><%--
  Created by IntelliJ IDEA.
  User: Sandro
  Date: 18-Jun-17
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="hike_home main-content" id="home">
    <div class="description-block">
        <div class="description-wrapper">
            <div class="description-header">
                <div class="description-header__left">
                    <%
                        DefaultModel defaultModel = (DefaultModel) request.getAttribute(DefaultModel.ATTR);
                        Integer loggedInUser = (Integer) request.getSession().getAttribute("userID");
                        int loggedInUserId = loggedInUser;
                        boolean IdMatch = (loggedInUserId == defaultModel.getCreator().getId());
                        String hikeName = defaultModel.getName();
                    %>
                    <div v-if="editModesOn.name">
                        <div class="darker-background"></div>
                        <form @submit.prevent="saveInfo('name')" action="">
                            <input type="text" v-model="name" class="edit-input edit-input-name" :style="{ width: '300px' }"
                                   @keydown="resizeEditInput(event, 'name')"
                                   @blur="saveInfo('name')"
                            >
                        </form>
                    </div>
                    <template v-else>
                        {{name}}
                        <button class="icon-btn dark" v-if="creatorLoggedIn" @click="editInfo('edit-input-name', 'name')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                    </template>
                </div>
                <div class="description-header__right">
                            <span title="ადამიანების მაქსიმალური რაოდენობა" style="margin-right: 30px;"><i
                                    class="fa fa-user" aria-hidden="true"></i> {{aboutModel.maxPeople}} </span>
                    <span title="გამგზავრების თარიღი" style="margin-right: 20px;"><i class="fa fa-arrow-up"
                                                                                     aria-hidden="true"></i> {{aboutModel.startDate | cutTime}}</span>
                    <span title="ჩამოსვლის თარიღი"><i class="fa fa-arrow-down" aria-hidden="true"></i> {{aboutModel.endDate | cutTime}}</span>
                </div>
            </div>
            <div class="description-body">
                <div style="margin-bottom: 10px;font-weight:bold; display: inline-block;">Description:</div>
                <div v-if="editModesOn.description">
                    <div class="darker-background"></div>
                    <form @submit.prevent="saveInfo('description')" action="" style="width: 100%;">
                        <textarea type="text"  v-model="aboutModel.description" class="edit-input edit-input-description" :style="{ width: '100%', height: '120px' }"
                               @keydown="resizeEditInput(event, 'description')"
                               @blur="saveInfo('description')">
                        </textarea>

                    </form>
                </div>
                <template v-else>
                    <button class="icon-btn dark" v-if="creatorLoggedIn" @click="editInfo('edit-input-description', 'description')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                    <br>
                    {{aboutModel.description}}
                </template>

            </div>
        </div>
        <div class="comments-count">{{aboutModel.comments.length}} comment<span v-show="aboutModel.comments.length != 1">s</span></div>
        <div class="comments-block">
            <div class="comments-block-inner">
                <ul class="comments-list">
                    <li class="comment" v-for="(comment, index) in aboutModel.comments">
                        <div class="avatar-block"
                             @mouseenter="profilePopupVue.hoverUser(comment.user, event)" @mouseleave="profilePopupVue.hoverOutUser(event)"
                             @click="window.location = '/Profile?userID=' +  comment.user.id"
                             :style="{ backgroundImage: 'url(' + comment.user.profilePictureAddress + ')' }">

                        </div>
                        <div class="comment-info">
                            <div class="comment-info__upper">
                                <div class="comment-author"
                                     @mouseenter="profilePopupVue.hoverUser(comment.user, event)" @mouseleave="profilePopupVue.hoverOutUser(event)"
                                     @click="window.location = '/Profile?userID=' +  comment.user.id">
                                    <span>{{comment.user.firstName}} </span><span>{{comment.user.lastName}}</span>
                                </div>
                            </div>
                            <div class="comment-info__lower">
                                <div class="comment-time">{{comment.date | cutTime}}
                                </div>
                                <div class="like-block">
                                    <i class="fa fa-thumbs-up" v-bind:class="{ liked: comment.isLiked }"
                                       v-on:click="like(comment.commentID)" aria-hidden="true"></i>
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
                        <div class="avatar-block" v-bind:style="{ backgroundImage: 'url(' + user.profilePictureAddress + ')' }">
                        </div>
                        <form action="HikePageServlet" v-on:submit.prevent="sendComment" method="post">
                            <input class="comment-input" type="text" autocomplete="off" name="add-comment"
                                   placeholder="Write a comment..." v-model="newCommentInput">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    var ws = new WebSocket("ws://localhost:8080/HikeCommentsSocket/" + hikeId);

    function css( element, property ) {
        return window.getComputedStyle( element, null ).getPropertyValue( property );
    }

    function textAreaAdjust(o) {
        o.style.height = "1px";
        o.style.height = (15 + o.scrollHeight)+"px";
    }

    var app = new Vue({
        el: '#vueapp',
        //These are stored instance variables for vue,
        //it will use these to bind element data and
        //modify them.
        data: {
            aboutModel: {},
            newCommentInput: "",
            profilePopupVue: profilePopupVue,
            editDescriptionPopupIsActive: false,
            editNamePopupIsActive: false,
            creatorLoggedIn: <%= IdMatch %>,
            name: "<%= hikeName.trim() %>",
            nameOld: "",
            descriptionOld: "",
            editModesOn: {}
        },
        //These functions will be called when page loads.
        created: function () {
            this.fetchData()
        },

        //These are stored methods that vue will be able to use.
        methods: {
            fetchData: function () {
                var th = this;
                axios.post("/HikeCommentsServlet?hikeId=" + hikeId, {}).then(function (response) {
                    th.aboutModel = response.data;
                });
            },
            //This method is invoked automatically when socket
            //server sends messageto this session.
            getSocketMessage: function (data) {
                var jsonData = JSON.parse(data.data);
                var action = jsonData.action;
                data = jsonData.data;
                if (action === "getComment") {
                    this.aboutModel.comments.push(data);
                } else if (action === "getCommentLike") {
                    var comment = this.aboutModel.comments.find(x => x.commentID == data.commentID);
                    if (data.liked) {
                        comment.likeNumber++;
                        if (data.userID == user.id) {
                            comment.isLiked = true;
                        }
                    } else if (comment.likeNumber > 0) {
                        comment.likeNumber--;
                        if (data.userID == user.id) {
                            comment.isLiked = false;
                        }
                    }
                }
            },
            //Sends new comment to socket server, called when enter is hit on comment.
            sendComment: function () {
                if(this.newCommentInput == ""){
                    return;
                }
                ws.send(JSON.stringify({
                    action: "getComment",
                    data: {
                        comment: this.newCommentInput,
                    }
                }));
                this.newCommentInput = "";
            },
            //This function is called when like button is clicked.
            like: function (commentId) {
                ws.send(JSON.stringify({
                    action: "getCommentLike",
                    data: {
                        commentID: commentId + "",
                    }
                }));
            },

            closeEditDescription: function () {
                this.editDescriptionPopupIsActive = false;
            },

            editDescription: function(){
//                var th = this;
//                axios({url: "/EditHikeDescription", method:"post", params:{hikeId: hikeId, description: this.newDescription}});
//                this.aboutModel.description = this.newDescription;
//                this.newDescription = "";
//                this.closeEditDescription();
            },

            editInfo: function (inputClassNm, varName) {
                Vue.set(this.editModesOn, varName, true);
                setTimeout(function(){
                    document.querySelector('.' + inputClassNm).focus();
                }, 1);
            },

            closeEditName: function () {
                this.editNamePopupIsActive = false;
            },

            saveInfo: function(varName){
                var th = this;
                var value = varName == 'name' ? th[varName] : th.aboutModel[varName];
                if(varName == 'name'){
                    axios({url: "/EditHikeName", method:"post", params:{hikeId: hikeId, name: value.trim()}});
                }else {
                   axios({url: "/EditHikeDescription", method:"post", params:{hikeId: hikeId, description: value.trim()}});

                }
                th.cancelEdit(varName);

            },

            editName: function(){

            },
            resizeEditInput: function(e, varName){
                if(varName != 'name'){
                    textAreaAdjust(e.target);
                    if(e.keyCode == 13) {
                        this.saveInfo(varName);
                        return;
                    }
                }
                e.target.style.width =  this.getWidth(varName, e) + 20 + 'px';
            },
            getWidth: function (varName, e) {
                var div = document.createElement('div');
                div.style.display = 'inline-block;';
                div.style.position = 'absolute';
                var elem = e.target;
                div.style.fontSize = css(elem, 'font-size');
                div.style.fontFamily = css(elem, 'font-family');
                document.body.appendChild(div);
                div.innerHTML = varName == 'name' ? this[varName] : this.aboutModel[varName];;
                var rect = div.getBoundingClientRect();
                var res = rect.width;
                div.parentNode.removeChild(div);
                return res;
            },
            cancelEdit: function(varName){
                Vue.set(this.editModesOn, varName, false);
            }
        }
    });
    ws.onmessage = app.getSocketMessage;
</script>
<%--
  Created by IntelliJ IDEA.
  User: Saba
  Date: 22.06.2017
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<link href="https://fonts.googleapis.com/css?family=Montserrat|Quicksand" rel="stylesheet">--%>
<script> document.querySelectorAll('title')[0].innerHTML = 'Home'; </script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" href="/Content/css/bootstrap-datepicker.min.css">
<script src="/Scripts/bootstrap-datepicker.min.js"></script>
<link rel="stylesheet" href="/Content/css/common.css">
<link rel="stylesheet" href="/Content/css/main.css">

<div id="vueapp">

    <div class="main-content hike-container">
        <ul class="hikes-list">
            <li class="hike-item" v-for="hike in hikes">
                <div class="slider-block">
                    <div class="caption">
                        {{hike.coverPhotos[0] ? hike.coverPhotos[0].locationName : "sandro"}}
                    </div>
                </div>
                <div class="hike-info">
                    <div class="hike-info_upper">
                        <div class="hike-title">
                            <a v-bind:href="'/HikePage/Home?hikeId=' + hike.id"> {{hike.name}} </a>
                        </div>
                    </div>
                    <div class="hike-info_middle">
                        <div class="dates-block">
                        <span title="გამგზავრების თარიღი" style="margin-right: 20px;"><i class="fa fa-arrow-up"
                                                                                         aria-hidden="true"></i> {{hike.startDate | cutTime}}</span>
                            <span title="ჩამოსვლის თარიღი"><i class="fa fa-arrow-down" aria-hidden="true"></i> {{hike.endDate | cutTime}}</span>
                        </div>
                        <div class="hike-people" title="წამომსვლელთა რაოდენობა/მაქსიმალური რაოდენობა">
                            <i class="fa fa-user" aria-hidden="true"></i>
                            {{hike.joinedPeople}}/{{hike.maxPeople}}
                        </div>
                    </div>
                    <div class="hike-info_lower">
                        <div class="description-body">
                            {{hike.description}}
                        </div>
                    </div>
                    <button class="mybtn submit-request"> Send Request</button>
                </div>
            </li>
        </ul>
    </div>
    <button @click="showPopup()" class="mybtn show-popup-btn">Create New Hike</button>

    <div class="new-hike-popup" :class="{active : popupIsActive }">
        <div class="new-hike-title">Create Hike</div>
        <div class="new-hike-upper">
            <div class="input-block">
                <div class="input-header ">
                    Hike name
                </div>
                <input type="text" class="new-hike-input hike-name-input" v-model="newHike.name">
            </div>
            <div class="input-block">
                <div class="input-header">
                    Max people
                </div>
                <input type="text" class="new-hike-input" v-model="newHike.maxPeople">
            </div>
            <div class="input-block">
                <div class="input-header">
                    Start Date
                </div>
                <input type="text" class="new-hike-input datepicker startDate" v-model="newHike.startDate">
            </div>
            <div class="input-block">
                <div class="input-header">
                    End Date
                </div>
                <input type="text" class="new-hike-input datepicker endDate" v-model="newHike.endDate">
            </div>
        </div>
        <div class="new-hike-description">
            <div class="input-header ">
                Description
            </div>
            <textarea type="text" class="new-hike-input descr" v-model="newHike.description"></textarea>
        </div>
        <button class="mybtn" style="padding: 10px 100px;" @click="addHike()">Add</button>
        <div class="close-block" @click="closePopup()"><i class="fa fa-times" aria-hidden="true"></i></div>
    </div>
</div>

<script src="../Scripts/axios.min.js"></script>
<script src="../Scripts/vue.min.js"></script>



<script>

    Vue.filter('cutTime', function (value) {
        if (!value) return "";
        return value.substr(0, value.length - 3);
    });

    var app = new Vue({
        el: '#vueapp',
        data: {
            hikes: "",
            newHike: {
                name: "",
                maxPeople: "",
                startDate: "",
                endDate: "",
                description: ""
            },
            popupIsActive: false
        },

        created: function () {
            var th = this;
            axios.post("/HikesListServlet", {}).then(function (response) {
                th.hikes = response.data.reverse();
            });
        },
        mounted: function() {
            var th = this;
            $(".datepicker.startDate").datepicker({
                weekStart: 1,
                format: "dd/mm/yyyy"
            }).on("changeDate", (e) => {
                    th.newHike.startDate = e.target.value;
                }
            );
            $(".datepicker.endDate").datepicker({
                weekStart: 1,
                format: "dd/mm/yyyy"
            }).on("changeDate", (e) => {
                    th.newHike.endDate = e.target.value;
                }
            );
        },

        methods: {
            showPopup: function(){
                this.popupIsActive = true;
            },
            addHike: function(){
                var th = this;
                axios({url: "/AddHikeServlet", method: "post", params: th.newHike}).then(function(response){
                    window.location.reload();
                });

            },
            closePopup: function(){
                this.popupIsActive = false;
            }
        }


    });
</script>

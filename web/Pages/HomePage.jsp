<%--
  Created by IntelliJ IDEA.
  User: Saba
  Date: 22.06.2017
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script> document.querySelectorAll('title')[0].innerHTML = 'Home'; </script>
<div id='vueapp' class="main-content hike-container">
    <ul class="hikes-list">
        <li class="hike-item" v-for="hike in hikes">
            <div class="slider-block">
                <div class="caption">
                    <div v-for="ph in hike.coverPhotos">
                        {{ph.locationName}}
                    </div>
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
                <button class="submit-request"> Send Request</button>
            </div>
        </li>
    </ul>
</div>

<script>
    Vue.filter('cutTime', function (value) {
        if (!value) return "";
        return value.substr(0, value.length - 3);
    });

    var app = new Vue({
        el: '#vueapp',
        data: {
            hikes: ""
        },

        created: function () {
            var th = this;
            axios.post("/HikesListServlet", {}).then(function (response) {
                th.hikes = response.data;
                console.log(th.hikes[0].coverPhotos[0].locationName);
            });
        },

        methods: {},


    });
</script>

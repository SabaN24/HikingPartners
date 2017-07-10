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
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<link rel="stylesheet" href="/Content/css/bootstrap-datepicker.min.css">
<script src="/Scripts/bootstrap-datepicker.min.js"></script>
<link rel="stylesheet" href="/Content/css/GoogleMaps.css">
<link rel="stylesheet" href="/Content/css/common.css">
<link rel="stylesheet" href="/Content/css/main.css">

<div class="searchItems">
    <div class="search-bars">
        <input id="location-search" type="text" placeholder="Search">
        <input id="hike-name-search" type="text" placeholder="Search">
        <input id="member-name-search" type="text" placeholder="Search">
        <input id="date-search" type="text" placeholder="Search" class="datepicker dateSearch" v-model="searchedDate" oninput="radioButtonChecks()">
    </div>
    <div class="radio-buttons">
        <form>
            <input type="radio" name="filter" id="location" checked="checked" onclick="radioButtonChecks()">Location
            <input type="radio" name="filter" id="hikeName" onclick="radioButtonChecks()">Hike Name
            <input type="radio" name="filter" id="memberName" onclick="radioButtonChecks()">Member Name
            <input type="radio" name="filter" id="date"  onclick="radioButtonChecks()">Date<p></p>
        </form>
    </div>


</div>

<div id="vueapp">

    <div class="main-content hike-container">
        <ul class="hikes-list">
            <li class="hike-item" v-for="hike in hikes">
                <div class="slider-block" v-bind:style="{'backgroundImage':'url(' + hike.coverPhotos[0].src + ')'}">
                    <div class="caption">
                        {{hike.coverPhotos[0].description}}
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

                    <button v-if="hike.joinedPeople < hike.maxPeople && !requestedHikeIds.includes(hike.id)" class="mybtn submit-request"
                            @click="sendRequest(hike.id)" onsubmit="return false;"> Send Request </button>
                    <div  v-if="hike.joinedPeople > hike.maxPeople" class="mybtn submit-request">Hike is full</div>
                    <div v-if="requestedHikeIds.includes(hike.id)" class="mybtn submit-request">Request sent</div>

                </div>
            </li>
        </ul>
    </div>
    <button @click="showPopup()" class="mybtn show-popup-btn">Create New Hike</button>

    <div class="new-hike-popup" :class="{active : popupIsActive }">
        <div class="edit-img-popup" v-show="popupImgShow">
            <div class="popup-img">
                <img v-bind:src="popupImg.src" alt="">
            </div>
            <div class="description-area-block">
                <div class="input-header ">
                    Description
                </div>
                <textarea v-model="editImgDescription"></textarea>
                <button class="mybtn" @click="submitImgDescription">Submit</button>
            </div>
        </div>
        <div v-show="newHikePage == 1">
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
            <div class="new-hike-pictures">
                <div class="input-header ">
                    Cover photos
                </div>
                <form action="" onsubmit="return false;" id="form-pictures"></form>
                <div class="new-post-img">
                    <img src="/Content/img/uploadPicture.png" @click="addPicture()" alt="">
                </div>
                <span class="new-hike-added-images" v-for="(picture, index) in pictures">
                    <div class="new-post-img">
                        <img src="/Content/img/uploadPicture.png" v-bind:src="picture.src" alt="">
                        <div class="photo-remove-button" @click="removePicture(index)"><i class="fa fa-window-close"></i></div>
                        <div class="edit-img-block" @click="openImgDescription(index)">
                            <i class="fa fa-pencil fa-2" aria-hidden="true"></i>
                        </div>
                    </div>
                </span>
            </div>
            <button class="mybtn" style="padding: 10px 100px;" @click="newHikePage++">Next</button>
        </div>
        <div class="map-container" v-show="newHikePage == 2">
            <h3>Choose hiking locations</h3>
            <div id="map"></div>
            <button class="mybtn" style="padding: 10px 100px;" @click="addHike">Add Hike</button>

        </div>
        <div class="close-block" @click="closePopup()"><i class="fa fa-times" aria-hidden="true"></i></div>
    </div>
</div>

<p id="noSearchData"></p>

<script src="../Scripts/axios.min.js"></script>
<script src="../Scripts/vue.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAN41T3N0B5Tx61omm8n9ZX6quK4FvG1jk&libraries=places&callback=initAutocomplete"
async defer></script>





<script>
    var locationSearch = document.getElementById("location-search");
    var hikeNameSearch = document.getElementById("hike-name-search");
    var memberNameSearch = document.getElementById("member-name-search");
    var dateSearch = document.getElementById("date-search");
    radioButtonChecks();


    var data = ""; //searched location data.
    hideShowSearchBars("location-search", "hike-name-search", "member-name-search", "date-search");
    function hideShowSearchBars(block, none1, none2, none3){
        document.getElementById(block).style.display = 'block';
        document.getElementById(none1).style.display = 'none';
        document.getElementById(none2).style.display = 'none';
        document.getElementById(none3).style.display = 'none';
    }

    function radioButtonChecks() {
        if(document.getElementById("location").checked){
            hideShowSearchBars("location-search", "hike-name-search", "member-name-search", "date-search");
            locationSearch.addEventListener("input", function () {
                if (locationSearch.value === "") {
                    app.redisplayAllHike();
                    return;
                }
                var hikeSearchBox = new google.maps.places.SearchBox(locationSearch);
                hikeSearchBox.addListener('places_changed', function () {

                    var places = hikeSearchBox.getPlaces();
                    var locationLat = 92.0;
                    var locationLng = 182.0;

                    if (places.length != 0) {
                        locationLat = places[Object.keys(places)[0]].geometry.location.lat();
                        locationLng = places[Object.keys(places)[0]].geometry.location.lng();
                    }
                    data = JSON.stringify({lat: locationLat + '', lng: locationLng + '', option: "location"});

                    app.locationSearch();

                });
            });
        }else if(document.getElementById("hikeName").checked){
            hideShowSearchBars("hike-name-search", "location-search", "member-name-search", "date-search");
            hikeNameSearch.addEventListener("input", function () {
                if (hikeNameSearch.value === "") {
                    app.redisplayAllHike();
                    return;
                }
                data = JSON.stringify({hikeName: hikeNameSearch.value + '', option: "hikeName"});

                app.hikeNameSearch();

            });
        }else if(document.getElementById("memberName").checked){

            hideShowSearchBars("member-name-search", "hike-name-search", "location-search",  "date-search");

            memberNameSearch.addEventListener("input", function () {
                if (memberNameSearch.value === "") {
                    app.redisplayAllHike();
                    return;
                }
                data = JSON.stringify({memberName: memberNameSearch.value + '', option: "memberName"});

                app.memberNameSearch();
            });
        }else if(document.getElementById("date").checked){
            hideShowSearchBars("date-search", "member-name-search", "hike-name-search", "location-search");
        }
    }


    var locationsArray = [];
    var input = document.createElement("input");
    input.setAttribute("id", "pac-input");
    input.setAttribute("type", "text");
    input.setAttribute("placeholder", "Search Box");


    function initAutocomplete() {
        // Create the search box and link it to the UI element.

        var center = {lat: 42.23333, lng: 43.96667};
        var map = new google.maps.Map(document.getElementById('map'), {
            center: center,
            zoom: 7,
            mapTypeId: 'roadmap'
        });
        var searchBox = new google.maps.places.SearchBox(input);
        map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

        google.maps.event.addListener(map, 'click', function(event) {
            placeMarker(event.latLng);
        });

        function placeMarker(location) {
            var markedLocation = {
                lat: location.lat(),
                lng: location.lng()
            };
            locationsArray.push(markedLocation);


            // The rest of this code assumes you are not using a library.
            // It can be made less wordy if you use one.

            var marker = new google.maps.Marker({
                position: location,
                draggable:true,
                map: map
            });

            var startLoc = {
                lat: "",
                lng: ""
            }


            google.maps.event.addListener(marker, 'dragstart', function(evt) {
                startLoc.lat = evt.latLng.lat();
                startLoc.lng = evt.latLng.lng();
            });
            google.maps.event.addListener(marker, 'dragend', function(evt) {
                var endLoc = {
                    lat: evt.latLng.lat(),
                    lng: evt.latLng.lng()
                }
                for(var i=0; i<locationsArray.length; i++){
                    if(JSON.stringify(locationsArray[i]) === JSON.stringify(startLoc)){
                        delete locationsArray[i];
                        locationsArray.splice(i, 1, endLoc);
                    }
                }
            });
        }

        // Bias the SearchBox results towards current map's viewport.
        map.addListener('bounds_changed', function() {
            searchBox.setBounds(map.getBounds());
        });


        var markers = [];

        //Hikes Search Box Listener.


        // Listen for the event fired when the user selects a prediction and retrieve
        // more details for that place.
        searchBox.addListener('places_changed', function() {
            var places = searchBox.getPlaces();

            if (places.length == 0) {
                return;
            }
            // Clear out the old markers.
            markers.forEach(function(marker) {
                marker.setMap(null);
            });

            markers = [];

            // For each place, get the icon, name and location.
            var bounds = new google.maps.LatLngBounds();
            places.forEach(function(place) {
                if (!place.geometry) {
                    console.log("Returned place contains no geometry");
                    return;
                }
                var icon = {
                    url: place.icon,
                    size: new google.maps.Size(1, 1),
                    origin: new google.maps.Point(0, 0),
                    anchor: new google.maps.Point(17, 34),
                    scaledSize: new google.maps.Size(25, 25)
                };

                placeMarker(place.geometry.location);
                // Create a marker for each place.
                // Only geocodes have viewport.

                markers.push(new google.maps.Marker({
                    map: map,
                    icon: icon,
                    title: place.name,
                    position: place.geometry.location
                }));
                if (place.geometry.viewport) {
                    bounds.union(place.geometry.viewport);
                } else {
                    bounds.extend(place.geometry.location);
                }
            });
            map.fitBounds(bounds);

        });
        google.maps.event.addListenerOnce(map, 'mouseover', function() {
            google.maps.event.trigger(map, 'resize');
            map.panTo(center);
        });

    }
    function post(path, params, method) {
        method = method || "post"; // Set method to post by default if not specified.

        // The rest of this code assumes you are not using a library.
        // It can be made less wordy if you use one.
        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", path);
        for(var i=0; i<params.length; i++){
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", i + "");
            hiddenField.setAttribute("value", JSON.stringify(params[i]));
            form.appendChild(hiddenField);
        }
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "LocationsListSize");
        hiddenField.setAttribute("value", params.length);
        form.appendChild(hiddenField);

        document.body.appendChild(form);
        form.submit();
    }

    function addLocation() {
        post("/LocationsServlet", locationsArray);
    }

    function reload() {
        locationsArray = [];
        initAutocomplete();
    }


    Vue.filter('cutTime', function (value) {
        if (!value) return "";
        return value.substr(0, value.length - 6);
    });

    var app = new Vue({
        el: '#vueapp',
        data: {
            hikes: "",
            searchedDate: "",
            newHike: {
                name: "",
                maxPeople: "",
                startDate: "",
                endDate: "",
                description: ""
            },
            requestedHikeIds: [],
            popupIsActive: false,
            newHikePage: 1,
            pictures: [],
            popupImgShow: false,
            popupImg: "",
            editImgDescription: "",
            popupImgIndex: 0
        },

        created: function () {
            var th = this;
            axios.post("/HikesListServlet", {}).then(function (response) {
                th.hikes = response.data.reverse();
            });
            axios.post("/GetRequestedHikeIds", {}).then(function (response) {
                th.requestedHikeIds = response.data;
            });
        },
        mounted: function() {
            var th = this;
            $(".datepicker.startDate").datepicker({
                weekStart: 1,
                format: "dd/mm/yyyy",
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
            $(".datepicker.dateSearch").datepicker({
                weekStart: 1,
                format: "dd/mm/yyyy",
                multidate: true
            }).on("changeDate", (e) => {
                    var th = this;
                    th.searchedDate = e.target.value;
                    console.log(th.searchedDate);
                    if(th.searchedDate.length >= 21){
                        data = JSON.stringify({date: th.searchedDate + '', option: "date"});
                        axios({url:"/HikeSearchServlet", method:"post", params:{data:data}}).then(function (response) {
                            th.hikes = response.data.reverse();
                            if(th.hikes.length == 0){
                                document.getElementById("noSearchData").innerHTML = "No results for that name";
                            }else{
                                document.getElementById("noSearchData").innerHTML = ""
                            }
                        });
                    }
                }
            );
        },

        methods: {
            locationSearch: function () {
                var th = this;
                axios({url:"/HikeSearchServlet", method:"post", params:{data:data}}).then(function (response) {
                    th.hikes = response.data.reverse();
                    if(th.hikes.length == 0){
                        document.getElementById("noSearchData").innerHTML = "No results for that location";
                    }else{
                        document.getElementById("noSearchData").innerHTML = ""
                    }
                });
            },

            redisplayAllHike: function () {
                var th = this;
                axios.post("/HikesListServlet", {}).then(function (response) {
                    th.hikes = response.data.reverse();
                    document.getElementById("noSearchData").innerHTML = ""
                });
            },

            hikeNameSearch: function () {
                var th = this;
                axios({url:"/HikeSearchServlet", method:"post", params:{data:data}}).then(function (response) {
                    th.hikes = response.data.reverse();
                    if(th.hikes.length == 0){
                        document.getElementById("noSearchData").innerHTML = "No results for that name";
                    }else{
                        document.getElementById("noSearchData").innerHTML = ""
                    }
                });
            },

            memberNameSearch: function () {
                var th = this;
                axios({url:"/HikeSearchServlet", method:"post", params:{data:data}}).then(function (response) {
                    th.hikes = response.data.reverse();
                    if(th.hikes.length == 0){
                        document.getElementById("noSearchData").innerHTML = "No results for that name";
                    }else{
                        document.getElementById("noSearchData").innerHTML = ""
                    }
                });
            },


            showPopup: function(){
                this.popupIsActive = true;
            },
            addHike: function(){
                var th = this;
                axios({url: "/AddHikeServlet", method: "post", params: th.newHike}).then(function(response){
                    th.addLocation(response.data.hikeID + '');
                });

            },
            closePopup: function(){
                this.newHike = {};
                this.newHikePage = 1;
                this.popupIsActive = false;
                this.pictures = [];
                this.popupImgShow = false;
                this.popupImg = "";
                this.editImgDescription = "";
                this.popupImgIndex = 0;
                document.querySelector("#form-pictures").innerHTML = "";
                reload();
            },

            sendRequest: function(hikeId){
                axios.post("/SendRequest?hikeId=" + hikeId, {});
                this.requestedHikeIds.push(hikeId);
            },

            addLocation: function(hikeID){
                var self = this;
                var data = JSON.stringify({hikeID:hikeID, locations:locationsArray});
                axios({url:"/LocationsServlet", method:"post", params:{data:data}}).then(function(response){
                    self.uploadPictures(hikeID);
                });
            },

            addPicture: function(){
                var input = document.createElement("input");
                var self = this;
                input.setAttribute("name", "pictures");
                input.setAttribute("type", "file");
                input.setAttribute("style", "display:none;");
                input.setAttribute("accept", "image/*");
                var descriptionInput = document.createElement("input");
                descriptionInput.setAttribute("name", "descriptions");
                descriptionInput.setAttribute("type", "hidden");
                descriptionInput.setAttribute("value", "");
                document.querySelector("#form-pictures").insertBefore(input, document.querySelector("#form-pictures").children[self.pictures.length]);
                document.querySelector("#form-pictures").insertBefore(descriptionInput, document.querySelector("#form-pictures").children[0]);
                input.click();
                input.onchange = function(event){
                    var input = event.target;
                    if (input.files && input.files[0]) {
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var picture = {
                                src: e.target.result
                            };
                            self.pictures.unshift(picture);
                        };
                        reader.readAsDataURL(input.files[0]);
                    }
                }
            },

            removePicture: function(index){
                var length = document.querySelector("#form-pictures").children.length / 2;
                document.querySelector("#form-pictures").removeChild(document.querySelector("#form-pictures").children[index]);
                document.querySelector("#form-pictures").removeChild(document.querySelector("#form-pictures").children[length + index - 1]);
                this.pictures.splice(index, 1);
            },

            uploadPictures: function(hikeID){
                var self = this;
                axios.post('/UploadCover?hikeID=' + hikeID, new FormData(document.querySelector("#form-pictures"))).then(function(response){
                    if(response.status == 200){
                        self.hikes.unshift(response.data);
                        self.closePopup();
                    }else{
                        self.uploadingPicture = false;
                    }
                });
            },

            submitImgDescription: function(){
                document.querySelector("#form-pictures").children[this.popupImgIndex].value = this.editImgDescription;
                this.popupImgShow = false;
            },

            openImgDescription: function(index){
                this.popupImgIndex = index;
                this.popupImg = this.pictures[index];
                var children = document.querySelector("#form-pictures").children;
                this.editImgDescription = children[index].value;
                this.popupImgShow = true;
            }

        }

    });
</script>

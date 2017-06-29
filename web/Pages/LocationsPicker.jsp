<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: vache
  Date: 6/27/2017
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<body>
<link rel="stylesheet" href="../Content/css/GoogleMaps.css">
<input id="pac-input" class="controls" type="text" placeholder="Search Box">
<div id="map"></div>

<script>

    var locationsArray = [];
    var input = document.getElementById('pac-input');
    function initAutocomplete() {
        // Create the search box and link it to the UI element.


        var map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: 42.23333, lng: 43.96667},
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


</script>

<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAN41T3N0B5Tx61omm8n9ZX6quK4FvG1jk&libraries=places&callback=initAutocomplete"
        async defer></script>

<button onclick="addLocation()">Add locations</button>
<button onclick="reload()">Remove markers</button>

</body>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>Places Searchbox</title>

</head>
</html>
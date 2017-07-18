<%@ page import="Database.LocationsDM" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Models.Location" %><%--
  Created by IntelliJ IDEA.
  User: vache
  Date: 6/29/2017
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="map" class="locations-block"></div>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAN41T3N0B5Tx61omm8n9ZX6quK4FvG1jk&libraries=places&callback=initAutocomplete"></script>

<script type="text/javascript">
    var locations = [
    ];

    <%
         LocationsDM locationsDM = LocationsDM.getInstance();
         ArrayList<Location> locsList = locationsDM.getLocationsByHikeID(Integer.parseInt(request.getParameter("hikeId")));
         for(Location loc : locsList){

     %>
            var eachLoc = [];
            eachLoc.push(<%= loc.getLatitude() %>);
            eachLoc.push(<%= loc.getLongitude() %>);
            locations.push(eachLoc);

    <% } %>



    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 7,
        center: {lat: 42.23333, lng: 43.96667},
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    var infowindow = new google.maps.InfoWindow();

    var marker, i;

    for (i = 0; i < locations.length; i++) {
        console.log(location[i]);
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(locations[i][0], locations[i][1]),
            map: map
        });

        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infowindow.setContent(locations[i][0]);
                infowindow.open(map, marker);
            }
        })(marker, i));
    }
</script>

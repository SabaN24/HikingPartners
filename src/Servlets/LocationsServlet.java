package Servlets;

import Database.LocationsDM;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by vache on 6/29/2017.
 */
@WebServlet("/LocationsServlet")
public class LocationsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(request.getParameter("data"), Map.class);
        int hikeID = Integer.parseInt((String)data.get("hikeID"));
        List<Map<String, Object>> points = (List)data.get("locations");
        LocationsDM locationsDM = LocationsDM.getInstance();
        for (Map point : points) {
            String lat = point.get("lat") + "";
            String lng = point.get("lng") + "";
            locationsDM.connectHikeAndLocation(hikeID, lat, lng, 1);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Helper.view("HikePage", "Locations", request, response);
    }
}

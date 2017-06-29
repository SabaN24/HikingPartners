package Servlets;

import Database.LocationsDM;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vache on 6/29/2017.
 */
@WebServlet("/LocationsServlet")
public class LocationsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int size = Integer.parseInt(request.getParameter("LocationsListSize"));
        LocationsDM locationsDM = LocationsDM.getInstance();
        int hikeID = 1; // Integer.parseInt(request.getParameter("hikeId"));
        for(int i=0; i<size; i++){
            String eachLoc = request.getParameter(i+"");
            String lat = eachLoc.substring(7, eachLoc.indexOf(','));
            System.out.println(lat);
            String lng = eachLoc.substring(eachLoc.indexOf("lng")+5, eachLoc.length()-1);
            System.out.println(lng);
            locationsDM.connectHikeAndLocation(hikeID, lat, lng, 1);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Helper.view("HikePage", "Locations", request, response);
    }
}

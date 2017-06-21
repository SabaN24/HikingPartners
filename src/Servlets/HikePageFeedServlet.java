package Servlets;

import Database.DataManager;
import Models.Hike.DefaultModel;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;

/**
 * Created by Nodo on 6/19/2017.
 */
@WebServlet("/HikePage/Feed")
public class HikePageFeedServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataManager dataManager = DataManager.getInstance();
        int hikeId = 1;
        if(request.getParameter("hikeId") != null) {
            hikeId = Integer.parseInt(request.getParameter("hikeId"));
        }
        DefaultModel defaultModel = dataManager.getDefaultModel(hikeId);
        request.setAttribute(DefaultModel.ATTR, defaultModel);

        Helper.view("HikePage", "Feed", request, response);
    }
}

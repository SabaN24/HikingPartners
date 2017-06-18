package Servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import Database.DataManager;
import Models.Hike.AboutModel;
import Models.Hike.DefaultModel;

/**
 * Created by Saba on 12.06.2017.
 */
@WebServlet("/HikePageServlet")
public class HikePageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletContext sc = request.getServletContext();
        DataManager dataManager = (DataManager) sc.getAttribute(DataManager.ATTR);
        int hikeId = 1;
        if(request.getParameter("hikeId") != null) {
            hikeId = Integer.parseInt(request.getParameter("hikeId"));
        }
        DefaultModel defaultModel = dataManager.getDefaultModel(hikeId);
        request.setAttribute(DefaultModel.ATTR, defaultModel);

        Helper.view("HikePage", "Home", request, response);
    }
}

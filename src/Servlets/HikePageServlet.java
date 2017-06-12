package Servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import Database.DataManager;
import Models.Hike.AboutModel;

/**
 * Created by Saba on 12.06.2017.
 */
@WebServlet("/HikePageServlet")
public class HikePageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        ServletContext sc = request.getServletContext();
//        DataManager dataManager = (DataManager)sc.getAttribute(DataManager.ATTR);
//        int hikeId =  Integer.parseInt(request.getParameter("id"));
//        AboutModel aboutModel = dataManager.getAboutModel(hikeId);
//        request.setAttribute(AboutModel.ATTR, aboutModel);
        Helper.view("HikePage", request, response);
    }
}

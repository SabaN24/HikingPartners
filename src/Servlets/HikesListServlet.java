package Servlets;

import Database.DataManager;
import Models.Hike.HikeInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

/**
 * Created by Saba on 22.06.2017.
 */
@WebServlet("/HikesListServlet")
public class HikesListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataManager dataManager = DataManager.getInstance();
        List<HikeInfo> hikes = dataManager.getAllHikes();
        Gson gson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        String hikesList = gson.toJson(hikes);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(hikesList);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

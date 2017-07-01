package Servlets;

import Database.MainDM;
import Models.Hike.HikeInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Saba on 22.06.2017.
 */
@WebServlet("/HikesListServlet")
public class HikesListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MainDM mainDM = MainDM.getInstance();
        List<HikeInfo> hikes = mainDM.getHikes(-1);
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

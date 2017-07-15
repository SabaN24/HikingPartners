package Servlets;

import Database.MainDM;
import Models.Hike.HikeInfoExtended;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Saba on 22.06.2017.
 * Gets list of hikes created by user when called from front-end.
 */
@WebServlet("/HikesListServlet")
public class HikesListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MainDM mainDM = MainDM.getInstance();
        int userID = (Integer) request.getSession().getAttribute("userID");
        List<HikeInfoExtended> info = mainDM.getHomePageInfo(userID);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String message = gson.toJson(info);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(message);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

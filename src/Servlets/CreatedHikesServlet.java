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
 * Created by Saba on 27.06.2017.
 */
@WebServlet("/CreatedHikesServlet")
public class CreatedHikesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MainDM mainDM = MainDM.getInstance();
        String userId = request.getParameter("userID");
        if(userId == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        int id = Integer.parseInt(userId);
        List<HikeInfo> hikes = mainDM.getHikes(id);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String createdHikes = gson.toJson(hikes);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(createdHikes);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

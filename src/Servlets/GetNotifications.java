package Servlets;

import Database.HikeDM;
import Database.MainDM;
import Models.Notification;
import Models.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sandro on 11-Jul-17.
 */
@WebServlet("/GetNotifications")
public class GetNotifications extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MainDM dm = MainDM.getInstance();
        HttpSession session = request.getSession();
        Integer userID = (Integer) session.getAttribute("userID");
        if(userID == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        List<Notification> notifications = dm.getNotifications(userID);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String message = gson.toJson(notifications);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(message);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

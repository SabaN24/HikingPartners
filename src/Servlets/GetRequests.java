package Servlets;

import Database.HikeDM;
import Models.Hike.HikeInfo;
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
 * Created by Saba on 7/4/2017.
 */
@WebServlet("/GetRequests")
public class GetRequests extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HikeDM hikeDM = HikeDM.getInstance();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userID");
        if(userId == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        int id = userId;
        List<Request> receivedRequests = hikeDM.getRequestsOfUser(id);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String requests = gson.toJson(receivedRequests);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(requests);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

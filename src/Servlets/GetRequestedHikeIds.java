package Servlets;

import Database.HikeDM;
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
 * Created by Sandro on 04-Jul-17.
 */
@WebServlet("/GetRequestedHikeIds")
public class GetRequestedHikeIds extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userID = (int)session.getAttribute("userID");
        List<Integer> requestedHikeIds = HikeDM.getInstance().getRequestedHikeIds(userID);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String userMessage = gson.toJson(requestedHikeIds, List.class);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(userMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

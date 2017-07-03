package Servlets;

import Database.MainDM;
import Models.Hike.AboutModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Sandro on 17-Jun-17.
 */
@WebServlet("/HikeCommentsServlet")
public class HikeCommentsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MainDM mainDM = MainDM.getInstance();
        HttpSession httpSession = request.getSession();
        int loggedInUserId = (Integer)httpSession.getAttribute("userID");
        int hikeId = Integer.parseInt(request.getParameter("hikeId"));
        AboutModel aboutModel = mainDM.getAboutModel(hikeId, loggedInUserId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonAboutModel = gson.toJson(aboutModel);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(jsonAboutModel);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}


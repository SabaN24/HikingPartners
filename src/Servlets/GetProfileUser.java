package Servlets;

import Database.UserInfoDM;
import Models.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sandro on 27-Jun-17.
 */
@WebServlet("/GetProfileUser")
public class GetProfileUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID =  request.getParameter("userID");
        if(userID == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        UserInfoDM dm = UserInfoDM.getInstance();
        User profileUser = dm.getUserByID(Integer.parseInt(userID));
        if(profileUser == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        Gson gson = new Gson();
        String userMessage = gson.toJson(profileUser);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(userMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

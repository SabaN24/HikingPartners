package Servlets;

import Database.UserInfoDM;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Sandro on 26-Jun-17.
 */
@WebServlet("/Profile")
public class ProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = request.getParameter("userID");
        if(userID == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        User user = UserInfoDM.getInstance().getUserByID(Integer.parseInt(userID));
        if(user == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        request.setAttribute(User.ATTR, user);
        Helper.view("ProfilePage", request, response);
    }
}

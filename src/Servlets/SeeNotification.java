package Servlets;

import Database.MainDM;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sandro on 12-Jul-17.
 * Makes status of notification "seen". Gets called when user sees notification.
 */
@WebServlet("/SeeNotification")
public class SeeNotification extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String notIDstr = request.getParameter("notificationID");
        if(notIDstr == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        int notID = Integer.parseInt(notIDstr);
        MainDM.getInstance().seeNotification(notID);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

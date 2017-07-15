package Servlets;

import Database.ChatDM;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Nodo on 7/2/2017.
 * Servlet closes chat whenever called.
 */
@WebServlet("/CloseChatServlet")
public class CloseChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession ss  = request.getSession();
        Integer fromUSerId = (Integer)ss.getAttribute("userID");
        Integer toUserId = Integer.parseInt(request.getParameter("toUserId"));
        ChatDM.getInstance().closeChat(fromUSerId, toUserId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

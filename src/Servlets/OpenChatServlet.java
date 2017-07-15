package Servlets;

import Database.ChatDM;
import Models.Chat;
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
 * Created by Nodo on 7/2/2017.
 * Opens chat whenever called from front-end.
 */
@WebServlet("/OpenChatServlet")
public class OpenChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession ss  = request.getSession();
        Integer fromUSerId = (Integer)ss.getAttribute("userID");
        Integer toUserId = Integer.parseInt(request.getParameter("toUserId"));
        ChatDM.getInstance().openChat(fromUSerId, toUserId);
        Chat  chat  = ChatDM.getInstance().getChat(fromUSerId, toUserId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonChat = gson.toJson(chat);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(jsonChat);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

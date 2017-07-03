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
import java.io.IOException;
import java.util.List;

/**
 * Created by Nodo on 7/2/2017.
 */
@WebServlet("/OpenChatsServlet")
public class OpenChatsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int fromUserId = Integer.parseInt(request.getParameter("userId"));
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<Chat> chats = ChatDM.getInstance().getChats(fromUserId);

        String jsonChats = gson.toJson(chats);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(jsonChats);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

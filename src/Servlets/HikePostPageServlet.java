package Servlets;

import Database.MainDM;
import Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Saba on 19.06.2017.
 */
@WebServlet("/HikePostPageServlet")
public class HikePostPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MainDM mainDM = MainDM.getInstance();
        HttpSession httpSession = request.getSession();
        int loggedInUser = (Integer)httpSession.getAttribute("userID");
        Integer id = Integer.parseInt(request.getParameter("hikeId"));
        List<Post> posts = mainDM.getPosts(id, loggedInUser);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String result = gson.toJson(posts);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

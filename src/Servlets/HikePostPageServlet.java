package Servlets;

import Database.DataManager;
import Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Saba on 19.06.2017.
 */
@WebServlet(name = "HikePostPageServlet")
public class HikePostPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        DataManager dataManager = (DataManager) sc.getAttribute(DataManager.ATTR);
        Integer id = (Integer) request.getAttribute("hikeID");
        List<Post> posts = dataManager.getPosts(id);
        Gson gson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        String result = gson.toJson(posts);
        request.setAttribute("posts", result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

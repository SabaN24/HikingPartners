package Servlets;

import Database.DataManager;
import Models.Hike.AboutModel;
import com.google.gson.Gson;
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
 * Created by Sandro on 17-Jun-17.
 */
@WebServlet("/HikeCommentsServlet")
public class HikeCommentsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        DataManager dataManager = (DataManager) sc.getAttribute(DataManager.ATTR);
        int hikeId = Integer.parseInt(request.getParameter("hikeId"));
        AboutModel aboutModel = dataManager.getAboutModel(hikeId);
        Gson gson = new Gson();
        String jsonAboutModel = gson.toJson(aboutModel);
        Map<String, Object> modelToSent = gson.fromJson(jsonAboutModel, Map.class);
        for(Object o : ((List)modelToSent.get("comments"))){
            ((Map)o).put("isLiked", false);
        }
        jsonAboutModel = gson.toJson(modelToSent);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(jsonAboutModel);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}


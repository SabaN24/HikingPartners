package Servlets;

import Database.HikeDM;
import Database.MainDM;
import Models.Photo;
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
 * Created by Levani on 12.07.2017.
 * Updates cover photo when called from front-end.
 */
@WebServlet("/UpdateImg")
public class UpdateImgServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int photoID = Integer.parseInt(request.getParameter("imgID"));
        int hikeID = Integer.parseInt(request.getParameter("hikeID"));
        String description = request.getParameter("description");
        HikeDM.getInstance().updateCoverPhoto(photoID, description, null);
        List<Photo> photos = MainDM.getInstance().getCoverPhotos(hikeID);
        Gson gson = new GsonBuilder().serializeNulls().create();
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(gson.toJson(photos));
    }
}

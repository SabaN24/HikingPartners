package Servlets;

import Database.GalleryDM;
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
 * Created by Levani on 11.07.2017.
 */
@WebServlet("/GetGalleryServlet")
public class GetGalleryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int hikeID = Integer.parseInt(request.getParameter("hikeID"));
        List<Photo> photos = GalleryDM.getInstance().getGalleryPhotos(hikeID);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String result = gson.toJson(photos);
        response.getWriter().print(result);
    }
}

package Servlets;

import Database.HikeDM;
import Database.MainDM;
import Models.Hike.AboutModel;
import Models.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.event.HierarchyBoundsAdapter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Saba on 7/9/2017.
 * Edits hike description when pressed Edit button on page.
 */
@WebServlet("/EditHikeDescription")
public class EditHikeDescription extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userID");
        if(userId == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        int hikeId = Integer.parseInt(request.getParameter("hikeId"));
        String newDescription = request.getParameter("description");
        HikeDM hikeDM = HikeDM.getInstance();
        hikeDM.updateDescription(hikeId, newDescription);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

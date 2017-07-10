package Servlets;

import Database.HikeDM;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Saba on 7/11/2017.
 * Changes name of given hike whenever called from page.
 */
@WebServlet("/EditHikeName")
public class EditHikeName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userID");
        if(userId == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        int hikeId = Integer.parseInt(request.getParameter("hikeId"));
        String newName = request.getParameter("name");
        HikeDM hikeDM = HikeDM.getInstance();
        hikeDM.updateName(hikeId, newName);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

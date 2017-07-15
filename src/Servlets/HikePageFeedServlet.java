package Servlets;

import Database.MainDM;
import Models.Member;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nodo on 6/19/2017.
 * Redirects to feed of hike.
 */
@WebServlet("/HikePage/Feed")
public class HikePageFeedServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int hikeId = Integer.parseInt(request.getParameter("hikeId"));
        List<Member> hikeMembers = MainDM.getInstance().getHikeMembers(hikeId);
        Integer loggedInUser = (Integer) request.getSession().getAttribute("userID");
        int loggedInUserId = loggedInUser;
        boolean loggedInUserIsMember = false;
        for(int i = 0; i < hikeMembers.size(); i++){
            if(hikeMembers.get(i).getId() == loggedInUserId) {
                loggedInUserIsMember = true;
            }
        }
        if(loggedInUserIsMember){
            Helper.view("HikePage", "Feed", request, response);
        } else {
            Helper.view("HikePage", "Home", request, response);
        }
    }
}

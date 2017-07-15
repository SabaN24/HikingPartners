package Servlets;

import Database.MainDM;
import Models.Member;
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
 * Created by Sandro on 01-Jul-17.
 * Returns members of hike when called from front-end.
 */
@WebServlet("/GetHikeMembers")
public class GetHikeMembers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hikeID =  request.getParameter("hikeID");
        if(hikeID == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        MainDM dm = MainDM.getInstance();
        List<Member> members = dm.getHikeMembers(Integer.parseInt(hikeID));
        if(members.size() == 0){
            Helper.servlet("/Home", request, response);
            return;
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String userMessage = gson.toJson(members);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(userMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

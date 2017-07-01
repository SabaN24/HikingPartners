package Servlets;

import Database.DataManager;
import Database.UserInfoDM;
import Models.Member;
import Models.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by Sandro on 01-Jul-17.
 */
@WebServlet("/GetHikeMembers")
public class GetHikeMembers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hikeID =  request.getParameter("hikeID");
        if(hikeID == null){
            Helper.servlet("/Home", request, response);
            return;
        }
        DataManager dm = DataManager.getInstance();
        List<Member> members = dm.getHikeMembers(Integer.parseInt(hikeID));
        if(members.size() == 0){
            Helper.servlet("/Home", request, response);
            return;
        }
        Gson gson = new Gson();
        String userMessage = gson.toJson(members);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(userMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

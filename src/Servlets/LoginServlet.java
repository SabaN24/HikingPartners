package Servlets;

import Database.UserInfoDM;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

/**
 * Created by Levani on 19.06.2017.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accessToken = request.getParameter("accessToken");
            String answer = Helper.sendGet("https://graph.facebook.com/oauth/access_token_info?access_token=" + accessToken);
            //aqamde tu movida eseigi sheidzleba useris sheshveba an daregistrireba
            UserInfoDM dm = UserInfoDM.getInstance();
            String[] nameArr = request.getParameter("name").split(" ");
            String firstname = nameArr[0];
            String lastname = nameArr[1];
            String email = request.getParameter("email");
            Integer age_range_min = Integer.parseInt(request.getParameter("age_range_min"));
            Integer age_range_max = Integer.parseInt(request.getParameter("age_range_max"));
            String link = request.getParameter("link");
            Long id = Long.parseLong(request.getParameter("id"));
            String picture_url = request.getParameter("picture_url");
            String gender = request.getParameter("gender");
            String birthday = request.getParameter("birthday");
            Date birthDate = birthday.equals("undefined") ? null : new Date(300000);
            int userID = -1;
            if(dm.isUserRegistered(id)){
                userID = dm.getUserByFacebookkID(id).getId();
            }else {
                userID = dm.registerUser(id, firstname, lastname, picture_url, birthDate, gender, email);
            }
            HttpSession session = request.getSession();
            session.setAttribute("userID", userID);
            Helper.view("HomePage", request, response);
        }catch(Exception e){
            int x = 5;
            //anu vigac chalichobs shemosvlas vinc realurad fbti ar daloginebula da am shemtxvevashi ar unda shevushvat.
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Helper.view("LoginPage", request, response);
    }
}

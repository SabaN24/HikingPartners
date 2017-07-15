package Servlets;

import Database.UserInfoDM;

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
 * Registers (or logs in user in case they are registered) new user when user tries to login with facebook.
 * DoGet method redirects to login page.
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accessToken = request.getParameter("accessToken");
            Helper.sendGet("https://graph.facebook.com/oauth/access_token_info?access_token=" + accessToken);
            UserInfoDM dm = UserInfoDM.getInstance();
            String[] nameArr = request.getParameter("name").split(" ");
            String firstname = nameArr[0];
            String lastname = nameArr[1];
            String email = request.getParameter("email");
            Long id = Long.parseLong(request.getParameter("id"));
            String picture_url = request.getParameter("picture_url");
            String gender = request.getParameter("gender");
            String birthday = request.getParameter("birthday");
            Date birthDate = null;
            if(!birthday.equals("undefined")){
                String birthDayArr[] = birthday.split("/");
                String birthdaySql = birthDayArr[2] + "-" + birthDayArr[1] + "-" + birthDayArr[0];
                birthDate = Date.valueOf(birthdaySql);
            }
            String coverPicUrl = request.getParameter("cover_url");
            if(coverPicUrl.equals("undefined"))
                coverPicUrl = null;
            String link = request.getParameter("link");
            int userID = -1;
            if(dm.isUserRegistered(id)){
                userID = dm.updateUserInfo(id, firstname, lastname, picture_url, birthDate, gender, email,coverPicUrl, link);
            }else {
                userID = dm.registerUser(id, firstname, lastname, picture_url, birthDate, gender, email, coverPicUrl, link);
            }
            HttpSession session = request.getSession();
            session.setAttribute("userID", userID);
            //session.setAttribute("userID", 5);
            Helper.servlet("/Home", request, response);
        }catch(Exception e){
            int x = 5;
            doGet(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Helper.view("LoginPage", request, response);
    }
}

package Servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Levani on 19.06.2017.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getParameter("accessToken");
            String answer = Helper.sendGet("https://graph.facebook.com/oauth/access_token_info?access_token=EAADXpFu47sUBAFZBEc3ZBYIs5QDaZBMGRn7WldUdQiSKCMgQKCXZBUps5KaefG1mtZBklYb1lYgnlk6LoVZBAZCUQZC6kzWYx1ZCmnm4viCZBQW3zu5m5oVuDJJNK0LXbvlU6w9QmyAWeG9Tz7WTpJoEqKWdTt8jobQDZCrG9YFw5EfMV3esMFryDGZAefYIIrAL9JgZ");
            //aqamde tu movida eseigi sheidzleba useris sheshveba an daregistrireba
        }catch(Exception e){
            //anu vigac chalichobs shemosvlas vinc realurad fbti ar daloginebula da am shemtxvevashi ar unda shevushvat.
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Helper.view("LoginPage", request, response);
    }
}

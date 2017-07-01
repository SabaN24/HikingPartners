package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * Created by Levani on 01.07.2017.
 */
@WebServlet("/CancelPhoto")
public class CancelPhotoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("userID") == null){
            response.sendRedirect("/Login");
        }
        String photoPath = request.getParameter("photoPath");
        String fullPath = Helper.root(this) + photoPath;
        new File(fullPath).delete();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/Home");
    }
}

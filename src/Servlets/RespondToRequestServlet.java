package Servlets;

import Database.HikeDM;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Saba on 7/4/2017.
 * Sends response to request (about adding to hike) to database.
 */
@WebServlet("/RespondToRequest")
public class RespondToRequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String requestResponse = request.getParameter("response");
        HikeDM hikeDM = HikeDM.getInstance();
        hikeDM.respondToRequest(requestId, requestResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

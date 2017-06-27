package Servlets;

import Database.HikeManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Saba on 27.06.2017.
 */
@WebServlet("/AddHikeServlet")
public class AddHikeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int maxPeople = Integer.parseInt(request.getParameter("maxPeople"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String description = request.getParameter("description");
        HttpSession session = request.getSession();
        Integer creatorId = (Integer) session.getAttribute("userID");
        HikeManager hikeManager = HikeManager.getInstance();
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date start = null;
        Date end = null;
        try {
            start = formatter.parse(startDate);
            end = formatter.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int newHikeId = hikeManager.addNewHike(name, start, end, description, maxPeople, creatorId);
        Helper.servlet("/Home", request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

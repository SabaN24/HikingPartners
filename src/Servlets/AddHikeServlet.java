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
import java.sql.Date;

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

        Date startDateDate = null;
        Date endDateDate = null;
//        if(!startDate.equals("undefined")){
//            String birthDayArr[] = startDate.split("/");
//            String birthdaySql = birthDayArr[2] + "-" + birthDayArr[1] + "-" + birthDayArr[0];
//            startDateDate = Date.valueOf(birthdaySql);
//        }
//        if(!endDate.equals("undefined")){
//            String birthDayArr1[] = endDate.split("/");
//            String birthdaySql1 = birthDayArr1[2] + "-" + birthDayArr1[1] + "-" + birthDayArr1[0];
//            endDateDate = Date.valueOf(birthdaySql1);
//        }
        int newHikeId = hikeManager.addNewHike(name, new Date(30000), new Date(30000), description, maxPeople, creatorId);
        Helper.servlet("/Home", request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

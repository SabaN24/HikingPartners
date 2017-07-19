package Servlets;

import Database.HikeDM;
import com.google.gson.Gson;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saba on 27.06.2017.
 * Servlet adds hike whenever called from front-end.
 */
@WebServlet("/AddHikeServlet")
public class AddHikeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int maxPeople = Integer.parseInt(request.getParameter("maxPeople"));
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String description = request.getParameter("description");
        HttpSession session = request.getSession();
        Integer creatorId = (Integer) session.getAttribute("userID");


        DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = null, endDate = null;
        try {
            startDate = dt.parse(startDateStr);
            endDate = dt.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HikeDM hikeDM = HikeDM.getInstance();
        int hikeID = hikeDM.addNewHike(name, startDate, endDate, description, maxPeople, creatorId);
        Map<String, Object> map = new HashMap<>();
        map.put("hikeID", hikeID);
        response.getWriter().print(new Gson().toJson(map));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

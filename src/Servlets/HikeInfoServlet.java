package Servlets;

import Database.HikeDM;
import Models.Hike.HikeInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Levani on 12.07.2017.
 * Returns hike info whenever hikepage loads.
 */
@WebServlet("/HikeInfo")
public class HikeInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HikeInfo hikeInfo = HikeDM.getInstance().getHikeById(Integer.parseInt(request.getParameter("hikeID")));
        Gson gson = new GsonBuilder().serializeNulls().create();
        String result = gson.toJson(hikeInfo);
        response.getWriter().print(result);
    }
}

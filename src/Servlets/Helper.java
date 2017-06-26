package Servlets;

import Database.DataManager;
import Models.Hike.DefaultModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
* Gets name of jsp file, request, response and redirects request and response.
* */
public class Helper {
    public static void view(String page, HttpServletRequest request, HttpServletResponse response)  {
        HttpSession session = request.getSession();
        DataManager dataManager = DataManager.getInstance();

        Integer userID = (Integer)session.getAttribute("userID");

        if(!page.equals("LoginPage") ){
            if(userID == null){
                servlet("/Login", request, response);
                return;
            }
            request.setAttribute("loggedInUser", dataManager.getUserById(userID));
        }

        request.setAttribute("page", page + ".jsp");
        RequestDispatcher rd = request.getRequestDispatcher("/Pages/Layout.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void view(String page, String subPage, HttpServletRequest request, HttpServletResponse response)  {
        request.setAttribute("subPage", page + "_" + subPage + ".jsp");
        if(page.equals("HikePage")) {
            DataManager dataManager = DataManager.getInstance();
            int hikeId = 1;
            if(request.getParameter("hikeId") != null) {
                hikeId = Integer.parseInt(request.getParameter("hikeId"));
            }
            DefaultModel defaultModel = dataManager.getDefaultModel(hikeId);
            request.setAttribute(DefaultModel.ATTR, defaultModel);
            view(page, request, response);
        }else{
            view(page, request, response);
        }
    }

    public static void servlet(String servlet, HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect(servlet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}

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
import java.io.IOException;

/*
* Gets name of jsp file, request, response and redirects request and response.
* */
public class Helper {
    public static void view(String page, HttpServletRequest request, HttpServletResponse response)  {
        HttpSession session = request.getSession();
        DataManager dataManager = DataManager.getInstance();
        int userID = 3;
        if((Integer)session.getAttribute("userID") != null){
            userID = (Integer)session.getAttribute("userID");
            request.setAttribute("loggedInUser", dataManager.getUserById(userID));
        }

        //This should be deleted in future, when we'll have login page
        session.setAttribute("userID", userID);
        request.setAttribute("loggedInUser", dataManager.getUserById(userID));

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
}

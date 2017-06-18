package Servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* Gets name of jsp file, request, response and redirects request and response.
* */
public class Helper {
    public static void view(String page, HttpServletRequest request, HttpServletResponse response)  {
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
        view(page, request, response);
    }
}

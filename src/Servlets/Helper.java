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
    public static void view(String viewName, HttpServletRequest request, HttpServletResponse response)  {
        RequestDispatcher rd = request.getRequestDispatcher("/Pages/" + viewName + ".jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Servlets;

import Database.MainDM;
import Models.Hike.DefaultModel;
import Models.User;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
* Gets name of jsp file, request, response and redirects request and response.
*/
public class Helper {
    /**
     * Loads page with given name
     * @param page name of page
     * @param request
     * @param response
     */
    public static void view(String page, HttpServletRequest request, HttpServletResponse response)  {
        HttpSession session = request.getSession();
        MainDM mainDM = MainDM.getInstance();

        Integer userID = (Integer)session.getAttribute("userID");

        if(!page.equals("LoginPage") ){
            if(userID == null){
                servlet("/Login", request, response);
                return;
            }
            request.setAttribute("loggedInUser", mainDM.getUserById(userID));
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

    /**
     * Loads page with given subpage.
     * @param page name of page
     * @param subPage name of subpage
     * @param request
     * @param response
     */
    public static void view(String page, String subPage, HttpServletRequest request, HttpServletResponse response)  {
        request.setAttribute("subPage", page + "_" + subPage + ".jsp");
        if(page.equals("HikePage")) {
            MainDM mainDM = MainDM.getInstance();
            int hikeId = 1;
            if(request.getParameter("hikeId") != null) {
                hikeId = Integer.parseInt(request.getParameter("hikeId"));
            }
            DefaultModel defaultModel = mainDM.getDefaultModel(hikeId);
            request.setAttribute(DefaultModel.ATTR, defaultModel);
            view(page, request, response);
        }else{
            view(page, request, response);
        }
    }

    /**
     * Redirects request and response to given servlet.
     * @param servlet name of servlet
     * @param request
     * @param response
     */
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

    public static String root(Object o) {
        String path = o.getClass().getClassLoader().getResource("").getPath();
        String fullPath = null;
        try {
            fullPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String pathArr[] = fullPath.split("/WEB-INF/classes/");
        fullPath = pathArr[0].substring(0, pathArr[0].indexOf("out") - 1);
        String reponsePath = new File(fullPath).getPath() + File.separatorChar;
        return reponsePath + "/web";
    }

    public static void savePrefferedSize(File file){
        try{
            BufferedImage img = ImageIO.read(file);
            int originalHeight = img.getHeight();
            int originalWidth = img.getWidth();
            if(originalHeight > 1200 || originalWidth > 1200) {
                img = Scalr.resize(img, 1200);
            }
            String type = file.getAbsolutePath();
            type = type.substring(file.getAbsolutePath().indexOf('.') + 1);
            ImageIO.write(img, type, file);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

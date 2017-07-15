package Servlets;

import Database.HikeSearchDM;
import Database.LocationsDM;
import Models.Hike.HikeInfo;
import Models.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static jdk.nashorn.internal.objects.NativeMath.round;

/**
 * Created by vache on 7/7/2017.
 * Returns hikes which match searching parameters from home page.
 */
@WebServlet("/HikeSearchServlet")
public class HikeSearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(request.getParameter("data"), Map.class);
        HikeSearchDM hikeSearchDM = HikeSearchDM.getInstance();

        String option = (String)data.get("option");
        if(option.equals("location")) {
            searchByLocation(data, hikeSearchDM, request, response);
        }else if(option.equals("hikeName")){
            searchByName(data, hikeSearchDM, request, response);
        }else if(option.equals("memberName")){
            searchByMemberName(data, hikeSearchDM, request, response);
        }else if(option.equals("date")){
            searchByDate(data, hikeSearchDM, request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Searches hikes with given location parameters.
     * @param data data used for filtering
     * @param hikeSearchDM data manager
     * @param request http request
     * @param response http respnse
     */
    private void searchByLocation(Map<String, Object> data,  HikeSearchDM hikeSearchDM, HttpServletRequest request, HttpServletResponse response){
        String latToSearch = (String) data.get("lat");
        String lngToSearch = (String) data.get("lng");
        Double lat = (double) Math.round(Double.parseDouble(latToSearch) * 10) / 10;
        Double lng = (double) Math.round(Double.parseDouble(lngToSearch) * 10) / 10;
        Double latFrom = lat - 0.1;
        Double latTo = lat + 0.1;
        Double lngFrom = lng - 0.1;
        Double lngTo = lng + 0.1;
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByLocation(latFrom, latTo, lngFrom, lngTo);
        Gson gson1 = new GsonBuilder().serializeNulls().create();
        String searchedHikesStr = gson1.toJson(searchedHikes);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(searchedHikesStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches hikes with given hike name parameters.
     * @param data data used for filtering
     * @param hikeSearchDM data manager
     * @param request http request
     * @param response http respnse
     */
    public void searchByName(Map<String, Object> data,  HikeSearchDM hikeSearchDM, HttpServletRequest request, HttpServletResponse response) {
        String searchedHikeName = (String)data.get("hikeName");
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByHikeName(searchedHikeName);
        Gson gson1 = new GsonBuilder().serializeNulls().create();
        String searchedHikesStr = gson1.toJson(searchedHikes);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(searchedHikesStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches hikes with given member name parameters.
     * @param data data used for filtering
     * @param hikeSearchDM data manager
     * @param request http request
     * @param response http respnse
     */
    public void searchByMemberName(Map<String, Object> data,  HikeSearchDM hikeSearchDM, HttpServletRequest request, HttpServletResponse response){
        String searchedMemberName = (String)data.get("memberName");
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByMemberName(searchedMemberName);
        Gson gson1 = new GsonBuilder().serializeNulls().create();
        String searchedHikesStr = gson1.toJson(searchedHikes);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(searchedHikesStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches hikes with given date parameters.
     * @param data data used for filtering
     * @param hikeSearchDM data manager
     * @param request http request
     * @param response http respnse
     */
    public void searchByDate(Map<String, Object> data,  HikeSearchDM hikeSearchDM, HttpServletRequest request, HttpServletResponse response){
        String dateFromTo = (String)data.get("date");
        String dateFrom = dateFromTo.substring(0, dateFromTo.indexOf(","));
        String dateTo = dateFromTo.substring(dateFromTo.indexOf(",") + 1);
        String fromArr[] = dateFrom.split("/");
        String toArr[] = dateTo.split("/");
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByDate(fromArr[2] + "-" + fromArr[1] + "-" + fromArr[0], toArr[2] + "-" + toArr[1] + "-" + toArr[0]);
        Gson gson1 = new GsonBuilder().serializeNulls().create();
        String searchedHikesStr = gson1.toJson(searchedHikes);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(searchedHikesStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

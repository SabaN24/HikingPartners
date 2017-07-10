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
 */
@WebServlet("/HikeSearchServlet")
public class HikeSearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(request.getParameter("data"), Map.class);
        HikeSearchDM hikeSearchDM = HikeSearchDM.getInstance();

        String option = (String)data.get("option");
        if(option.equals("location")) {
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
            response.getWriter().print(searchedHikesStr);

        }else if(option.equals("hikeName")){
            String searchedHikeName = (String)data.get("hikeName");
            List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByHikeName(searchedHikeName);
            Gson gson1 = new GsonBuilder().serializeNulls().create();
            String searchedHikesStr = gson1.toJson(searchedHikes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(searchedHikesStr);

        }else if(option.equals("memberName")){
            String searchedMemberName = (String)data.get("memberName");
            List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByMemberName(searchedMemberName);
            Gson gson1 = new GsonBuilder().serializeNulls().create();
            String searchedHikesStr = gson1.toJson(searchedHikes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(searchedHikesStr);

        }else if(option.equals("date")){
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
            response.getWriter().print(searchedHikesStr);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

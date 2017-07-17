package Servlets;

import Database.HikeSearchDM;
import Database.UserInfoDM;
import Models.Hike.HikeInfo;
import Models.User;
import Tests.InputCheckers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

        String option = (String) data.get("option");

        if (option.equals("memberName")) {

            String name = (String) data.get("memberName");
            addSearchMember(name, response);

        } else if (option.equals("applyFilter") && InputCheckers.checkSearchedInputs(data)) {

            Set<HikeInfo> searchedHikes = new HashSet();

            /* filter by member number range*/
            List<HikeInfo> searchedByMemberNum = searchByRange(data, hikeSearchDM);
            for(HikeInfo hi: searchedByMemberNum){
                searchedHikes.add(hi);
            }

            /* filter by date */
            List<HikeInfo> searchedByDate = searchByDate(data, hikeSearchDM);
            joinListToSearchedSet(searchedHikes, searchedByDate);

            /* filter by hike Name */
            List<HikeInfo> searchedByName = searchByName(data, hikeSearchDM);
            joinListToSearchedSet(searchedHikes, searchedByName);


            /* filter by members */
            Gson gson1 = new Gson();
            List<Object> members = (ArrayList<Object>) (data.get("members"));
            for(Object obj: members){

                Map<String, Object> mp =  gson1.fromJson( gson.toJsonTree(obj).getAsJsonObject(), Map.class);
                List<HikeInfo> searchedByMember = searchByMember(mp, hikeSearchDM);
                joinListToSearchedSet(searchedHikes, searchedByMember);

            }

            /* filter by locations */
            List<Object> locations = (ArrayList<Object>) (data.get("searchedLocations"));
            for(Object obj : locations){

                Map<String, Object> mp = gson1.fromJson( gson.toJsonTree(obj).getAsJsonObject(), Map.class);
                List<HikeInfo> searchedByLocs = searchByLocation(mp, hikeSearchDM);
                joinListToSearchedSet(searchedHikes,searchedByLocs);

            }

            /* write respones */
            String res = gson.toJson(searchedHikes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().print(res);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if(option.equals("hikeName")){

            List<HikeInfo> addingHikes = searchByName( data,hikeSearchDM);

            String res = gson.toJson(addingHikes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().print(res);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Searches hikes with given location parameters.
     *
     * @param data    data used for filtering
     */
    private List<HikeInfo> searchByLocation(Map<String, Object> data, HikeSearchDM hikeSearchDM) {
        String latToSearch = (String) data.get("lat");
        String lngToSearch = (String) data.get("lng");
        Double lat = (double) Math.round(Double.parseDouble(latToSearch) * 10) / 10;
        Double lng = (double) Math.round(Double.parseDouble(lngToSearch) * 10) / 10;
        Double latFrom = lat - 0.1;
        Double latTo = lat + 0.1;
        Double lngFrom = lng - 0.1;
        Double lngTo = lng + 0.1;
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByLocation(latFrom, latTo, lngFrom, lngTo);

        return searchedHikes;

    }

    /**
     * Searches hike with number of hike members with given range.
     * @param data
     * @param hikeSearchDM
     * @return List of hikes which match given parameters
     */
    private List<HikeInfo> searchByRange(Map<String, Object> data,HikeSearchDM hikeSearchDM){
        String min = (String)data.get("minMembersNum");
        min = (min.equals("")) ? "-1" : min;
        String max  = (String)data.get("maxMembersNum");
        max = (max.equals("")) ? 1000000+"" : max;
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByMemberNumber(min, max);
        return searchedHikes;
    }


    /**
     * Searches hikes with given hike name parameter.
     * @param data data gained from front-end
     * @param hikeSearchDM data manager object
     * @return
     */
    public List<HikeInfo> searchByName(Map<String, Object> data, HikeSearchDM hikeSearchDM) {

        String searchedHikeName = (String) data.get("hikeName");
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByHikeName(searchedHikeName);

        return searchedHikes;

    }

    /**
     * Searches hikes with given member name parameter.
     *
     * @param data         data used for filtering
     * @param hikeSearchDM data manager
     */
    public List<HikeInfo>  searchByMember(Map<String, Object> data, HikeSearchDM hikeSearchDM) {
        int id =( (Double) data.get("id")).intValue();

        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByMemberName(id + "");
        return searchedHikes;
    }

    /**
     * Searches hikes with given date parameter.
     *
     * @param data         data used for filtering
     * @param hikeSearchDM data manager
     */
    public List<HikeInfo> searchByDate(Map<String, Object> data, HikeSearchDM hikeSearchDM) {

        String dateFrom = (String)data.get("startDate");
        dateFrom = (dateFrom.equals("")) ? "06/07/0001" : dateFrom;
        String dateTo = (String)data.get("endDate");
        dateTo = (dateTo.equals("")) ? "01/07/9999" : dateTo;
        String fromArr[] = dateFrom.split("/");
        String toArr[] = dateTo.split("/");
        List<HikeInfo> searchedHikes = hikeSearchDM.getSearchedHikesByDate(fromArr[2] + "-" + fromArr[1] + "-" + fromArr[0], toArr[2] + "-" + toArr[1] + "-" + toArr[0]);

        return searchedHikes;
    }


    /**
     * method joins given List 'addingHikes' to set hikes
     * after this method hikes has the elements that was common for this set and list
     * in another words finally hikes is overlap
     * @param hikes result set needed for joined hikes
     * @param addingHikes that hikes that should be added
     *
     */
    public void joinListToSearchedSet(Set<HikeInfo> hikes, List<HikeInfo> addingHikes){

        Set<HikeInfo> helper = new HashSet<>();

        for(HikeInfo hi :addingHikes){
            helper.add(hi);
        }

        hikes.retainAll(helper);

    }



    /**
     * Sends object of searched user to response.
     * @param name name of searched user
     * @param response http response
     */
    private void addSearchMember(String name, HttpServletResponse response) {

        UserInfoDM userInfoDM = UserInfoDM.getInstance();
        User user = userInfoDM.getUserByName(name);
        Gson gson1 = new GsonBuilder().serializeNulls().create();
        String searchedUser = gson1.toJson(user);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(searchedUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

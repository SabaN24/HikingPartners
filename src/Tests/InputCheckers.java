package Tests;

import com.google.gson.Gson;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Map;

/**
 * Created by vache on 7/17/2017.
 */
public class InputCheckers {

    /**
     * Checks input data to be in correct formats.
     * @param searchedData - data from inputs (maxMemberNum, MinMemberNum, StartDate, EndDate, etc.)
     * @return true if all item from data are in correct form, false - otherwise.
     */
    public static boolean checkSearchedInputs(Map<String, Object> searchedData){
        if(!searchedData.containsKey("option") || !searchedData.containsKey("minMembersNum")
                || !searchedData.containsKey("maxMembersNum") || !searchedData.containsKey("members")
                || !searchedData.containsKey("startDate") || !searchedData.containsKey("endDate")
                || !searchedData.containsKey("hikeName")) return false;
        String minNum = (String)searchedData.get("minMembersNum");
        String maxNum = (String)searchedData.get("maxMembersNum");
        String startDate = (String)searchedData.get("startDate");
        String endDate = (String)searchedData.get("endDate");
        if(!minNum.equals(""))
            if (!minNum.matches("[0-9]+") || Integer.parseInt(minNum) < 0) return false;

        if(!maxNum.equals(""))
            if (!maxNum.matches("[0-9]+") || Integer.parseInt(maxNum) < 0) return false;

        if(!startDate.equals(""))
            if (!dateChecker(startDate)) return false;

        if(!endDate.equals(""))
            if (!dateChecker(endDate)) return false;

        return true;
    }

    /**
     * Checking if date is in correct format
     * @param date as string
     * @return true if date is in correct format,  false - otherwise
     */
    private static boolean dateChecker(String date){
        String dateArr[] = date.split("/");
        if(dateArr.length != 3) return false;
        if(dateArr[0].length() > 2 || dateArr[0].length() == 0
                || dateArr[1].length() > 2 || dateArr[1].length() == 0
                || dateArr[2].length() > 4 || dateArr[2].length() == 0) return false;
        return true;
    }

    /**
     * Add new hike inputs checker.
     * @param name
     * @param maxPeople
     * @param startDate
     * @param endDate
     * @param creatorID
     * @return boolean.
     */
    public static boolean checkAddHikeInputs(String name, String maxPeople, String startDate, String endDate, int creatorID){
        if(name.length() > 50 || !maxPeople.matches("[0-9]+")
                || Integer.parseInt(maxPeople) > Integer.MAX_VALUE|| creatorID < 0) return false;
        if(!dateChecker(startDate) || !dateChecker(endDate)) return false;
        return true;
    }
}

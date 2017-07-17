package Tests;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by vache on 7/17/2017.
 */
public class TestInputCheckers {

    @Test
    public void testSearchChecker1() {
        //Tests for invalid map keys.

        Map<String, Object> data = new HashMap();
        data.put("option", "applyFilter");
        data.put("searchedLocations", "Batumi");
        data.put("minMembersNum", "1");
        data.put("maxMembersNum", "1");
        assertEquals(false, InputCheckers.checkSearchedInputs(data));

        data.put("members", "dot");
        data.put("startDate", "12/12/1992");
        data.put("endDate", "12/12/1993");
        data.put("hikeName", "hikeName");
        assertEquals(true, InputCheckers.checkSearchedInputs(data));
    }

    @Test
    public void testSearchChecker2() {
        //Tests for wrong date format

        Map<String, Object> data = new HashMap();
        data.put("option", "applyFilter");
        data.put("searchedLocations", "Batumi");
        data.put("minMembersNum", "5");
        data.put("maxMembersNum", "13");
        data.put("members", "dot");
        data.put("startDate", "123/dsdd/1992/ad/13");
        data.put("endDate", "/12/1993/3434");
        data.put("hikeName", "hikeName");
        assertEquals(false, InputCheckers.checkSearchedInputs(data));

        data.put("minMembersNum", "-5");
        data.put("maxMembersNum", "invalidNumber");
        data.put("startDate", "12/12/1992");
        data.put("endDate", "12/12/1993");
        assertEquals(false, InputCheckers.checkSearchedInputs(data));

        data.put("minMembersNum", "");
        data.put("maxMembersNum", "");
        data.put("startDate", "12/12/1992");
        data.put("endDate", "12/12/1993");
        assertEquals(true, InputCheckers.checkSearchedInputs(data));
    }

    @Test
    public void testAddHikeChecker1() {
        //Basic Tests for AddHikeChecker.

        String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertEquals(false, InputCheckers.checkAddHikeInputs(name, "5", "12/12/1992", "12/12/1993", 5));

        String maxPeople = "-1";
        assertEquals(false, InputCheckers.checkAddHikeInputs("name", maxPeople, "12/12/1992", "12/12/1993", 5));

        String date = "abcdefg";
        assertEquals(false, InputCheckers.checkAddHikeInputs("name", "5", date, date, 5));

        assertEquals(false, InputCheckers.checkAddHikeInputs("name", "5", "12/12/1992", "12/12/1993", -3));

        assertEquals(true, InputCheckers.checkAddHikeInputs("name", "5", "12/12/1992", "12/12/1993", 3));

    }
}

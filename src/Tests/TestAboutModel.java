package Tests;

import Models.Comment;
import Models.Hike.AboutModel;
import Models.MiniUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



/**
 * Created by Nodo on 6/11/2017.
 */
public class TestAboutModel {

    private AboutModel aboutModel;
    private DateFormat format;

    @BeforeEach
    public void testSetup(){
        String desc = "This  Hike is really really good!";
        String name = "NiceQvabisTavi";

        Date date1  = new Date();
        Date date2 = new Date();

        format = new SimpleDateFormat("d MMMM yyyy");
        try {
            date1 = format.parse("24 June 20017 ");
            date2 = format.parse("27 August 2017");
        } catch (ParseException e) {}


        List<Comment > commentList = new ArrayList<>();

        MiniUser user = new MiniUser(1, "bla", "bla", "bla");
        for(int i = 0; i < 100; i++)
            commentList.add(new Comment(i, "bla" + i, 0,user, date1, 10));

         aboutModel = new AboutModel(desc, date1, date2,5, commentList );


    }

    /**
     * test for getters of AboutModel
     */
    @Test
    public void testAboutModel(){

        assertEquals("This  Hike is really really good!", aboutModel.getDescription());
        assertEquals(100, aboutModel.getComments().size());
        assertEquals(5, aboutModel.getComments().get(5).getCommentID());
        assertEquals("bla7", aboutModel.getComments().get(7).getComment());
        assertEquals(5, aboutModel.getMaxPeople());

        Date date1  = new Date();
        Date date2 = new Date();

        DateFormat format = new SimpleDateFormat("d MMMM yyyy");
        try {
            date1 = format.parse("24 June 20017 ");
            date2 = format.parse("27 August 2017");
        } catch (ParseException e) {}

        assertEquals(date1, aboutModel.getStartDate());
        assertEquals(date2, aboutModel.getEndDate());


        AboutModel AboutModel2 = new AboutModel("bla", new Date(), new Date(),5, null);

        assertEquals(AboutModel2, aboutModel);


    }
}

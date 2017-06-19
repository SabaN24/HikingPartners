package Models.Hike;


import Models.Comment;
import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Nodo on 6/11/2017.
 */
public class AboutModel {

    private int ID;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private int maxPeople;
    private List<Comment> comments;

    public static final String ATTR = "AboutModel";


    /**
     * constructor of about of hike
     * @param ID
     * @param name
     * @param description
     * @param startDate
     * @param endDate
     * @param comments
     */
    public AboutModel(int ID, String name, String description, Date startDate, Date endDate,int maxPeople, List<Comment> comments) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.comments = comments;
        this.maxPeople = maxPeople;
    }


    /**
     * @return of hikes
     */
    public int getID() {
        return ID;
    }


    /**
     * @returns name of hike
     */
    public String getName() {
        return name;
    }


    /**
     * @returns starting date of hike
     */
    public Date getStartDate() {
        return startDate;
    }


    /*
     * @returns ending date of hike
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return description of hike
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return comments of about
     */
    public List<Comment> getComments() { return comments; }

    /**
     * @return maximum number of users that can go the trip
     */
    public int getMaxPeople() {
        return maxPeople;
    }

    /**
     * equals compares of aboutHike with their hike id
     * @param obj
     * @return true if ids are same and returns false if not
     */
    @Override
    public boolean equals(Object obj) {
        AboutModel other = (AboutModel) obj;
        return other.ID == this.ID;
    }}

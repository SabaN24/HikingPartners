package Models.Hike;

import Models.Photo;
import Models.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Saba on 22.06.2017.
 * Class represents information about hike, which will be seen in hike feed.
 */
public class HikeInfo extends DefaultModel {

    private int maxPeople;
    private int joinedPeople;
    private Date startDate;
    private Date endDate;
    private String description;


    /**
     * Constructor method.
     *
     * @param id
     * @param name
     * @param creator     User
     * @param coverPhotos List<Photo>
     */
    public HikeInfo(int id, String name, User creator, List<Photo> coverPhotos,
                    int maxPeople, int joinedPeople, Date start, Date end, String description) {
        super(id, name, creator, coverPhotos);
        this.maxPeople = maxPeople;
        this.joinedPeople = joinedPeople;
        this.startDate = start;
        this.endDate = end;
        this.description = description;
    }

    /**
     * @return maximum amount of people on this hike
     */
    public int getMaxPeople(){
        return maxPeople;
    }

    /**
     * @return current amount of people on this hike
     */
    public int getJoinedPeople(){
        return joinedPeople;
    }

    /**
     * @return start date of this hike
     */
    public Date getStartDate(){
        return startDate;
    }

    /**
     * @return end date of this hike
     */
    public Date getEndDate(){
        return endDate;
    }

    /**
     * @return description of this hike
     */
    public String getDescription(){
        return description;
    }

    @Override
    public String toString(){
        return (super.toString()) + " Max: " + getMaxPeople() + " Joined: " +
                getJoinedPeople() + " Start: " + getStartDate() + " End: " + getEndDate() + " Desc: " + getDescription();
    }

    @Override
    public boolean equals(Object secondHikeInfo){
        if(getId() == ((DefaultModel)secondHikeInfo).getId()) return true;
        return false;
    }


    @Override
    public int hashCode() {
        return ((Integer)this.getId()).hashCode();
    }

}

package Models.Hike;

import Models.Photo;
import Models.MiniUser;

import java.util.List;

/**
 * Created by Levani on 11.06.2017.
 */
public class DefaultModel {

    public static final String ATTR = "DefaultModel";

    /**
     * Private instance variables.
     */
    private int id;
    private String name;
    private MiniUser creator;
    private List<Photo> coverPhotos;

    /**
     * Constructor method.
     * @param creator User
     * @param coverPhotos List<Photo>
     */
    public DefaultModel(int id, String name, MiniUser creator, List<Photo> coverPhotos) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.coverPhotos = coverPhotos;
    }

    /**
     * @return id of hike
     */
    public int getId() {
        return id;
    }

    /**
     * @return name of hike
     */
    public String getName() {
        return name;
    }

    /**
     * Returns creator of hike.
     * @return User
     */
    public MiniUser getCreator() {
        return creator;
    }

    /**
     * Returns cover photos of hike.
     * @return List<Photo>
     */
    public List<Photo> getCoverPhotos() {
        return coverPhotos;
    }

    /**
     * equals compares of aboutHike with their hike id
     * @param obj
     * @return true if ids are same and returns false if not
     */
    @Override
    public boolean equals(Object obj) {
        DefaultModel other = (DefaultModel) obj;
        return other.id == this.id;
    }

    @Override
    public String toString(){
        return id + " " + name + " created by: " + creator.getFirstName() + " " + creator.getLastName();
    }

}

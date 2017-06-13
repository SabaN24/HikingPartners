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
    private MiniUser creator;
    private List<Photo> coverPhotos;

    /**
     * Constructor method.
     * @param creator User
     * @param coverPhotos List<Photo>
     */
    public DefaultModel(MiniUser creator, List<Photo> coverPhotos) {
        this.creator = creator;
        this.coverPhotos = coverPhotos;
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
}

package Models;

/**
 * Created by vache on 6/11/2017.
 */
public class Photo {
    private int ID;
    private String src;
    private String description;

    public Photo(int ID, String src, String description) {
        this.ID = ID;
        this.src = src;
        this.description = description;
    }

    /**
     * @return Photo ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return Photo address
     */
    public String getSrc(){
        return src;
    }


    /**
     * @return photo description
     */
    public String getDescription(){
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if(ID == ((Photo)obj).getID()) return true;
        return false;
    }
}

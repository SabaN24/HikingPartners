package Models;

/**
 * Created by Sandro on 21-Jun-17.
 * Keeps response of hike.
 */
public class HikeResponse {
    //Private variables
    private Object data;
    private String action;

    /**
     * Constructor of HikeResponse object.
     * @param data data of response
     * @param action action of response
     */
    public HikeResponse(Object data, String action){
        this.data = data;
        this.action = action;
    }
}

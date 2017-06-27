package Database;

/**
 * Created by Nodo on 6/27/2017.
 */
public class HikeManager {



    private DatabaseConnector databaseConnector;

    public static final String ATTR = "HikeManager";

    private static HikeManager hikeManager = null;

    private HikeManager() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    public static HikeManager getInstance() {
        if (hikeManager == null) {
            hikeManager = new HikeManager();
        }
        return hikeManager;
    }



}

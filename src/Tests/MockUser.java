package Tests;

import Models.User;

/**
 * Created by vache on 6/10/2017.
 */
public class MockUser extends User {
    private int ID;
    public MockUser(int ID){
        this.ID = ID;
    }

    public int getID(){
        return ID;
    }

    @Override
    public boolean equals(Object obj) {
        MockUser mu = (MockUser)obj;
        if(mu.getID() == ID) return true;
        return false;
    }
}

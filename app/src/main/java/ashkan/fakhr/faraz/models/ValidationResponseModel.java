package ashkan.fakhr.faraz.models;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */

public class ValidationResponseModel {

    boolean status;
    int user_id;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


}

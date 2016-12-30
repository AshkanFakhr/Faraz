package ashkan.fakhr.faraz.models;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */

public class LoginResponseModel {
    String token;
    LoggedInUserModel user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoggedInUserModel getUser() {
        return user;
    }

    public void setUser(LoggedInUserModel user) {
        this.user = user;
    }
}

package in.OrderCruch.Modal;

/**
 * Created by gautam on 9/6/16.
 */
public class ServerResponse {
    private String result;
    private String message;
    private User user;

    private User[] users;

    public User[] getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }


}

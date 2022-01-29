package ca.dal.csci3130.quickcash.usermanagement;

public interface SessionManagerInterface {

    void createLoginSession(String email, String password, String name);

    void checkLogin();

    void logoutUser();

    boolean isLoggedIn();

    String getKeyName();

    String getKeyEmail();
}

package ca.dal.csci3130.quickcash.usermanagement;

/**
 * Provides a session manager to store the session of a logged in user.
 * Only one SessionManager available to all activities.
 * Could be accessed as: SessionManager.getSessionManager(<context>)
 */
public interface SessionManagerInterface {

    /**
     * Create a login session and save shared preferences
     *
     * @param email
     * @param password
     * @param name
     * @param isEmployee
     */
    void createLoginSession(String email, String password, String name, boolean isEmployee);

    /**
     * Logout user
     */
    void logoutUser();

    /**
     * Check is a session is on-going
     *
     * @return true or false
     */
    boolean isLoggedIn();

    /**
     * Get name of the user that is logged in
     *
     * @return name
     */
    String getKeyName();

    /**
     * Get the email of the user that is logged in
     *
     * @return userEmail
     */
    String getKeyEmail();

    /**
     * Get whether the user is an employee or an employer
     *
     * @return false - employer, true - employee
     */
    boolean getIsEmployee();
}

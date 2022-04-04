package ca.dal.csci3130.quickcash.usermanagement;

/**
 * Preference Interface to store preferences of a user
 */
public interface PreferenceInterface {

    /**
     * Get the id of the user who owns these preferences
     *
     * @return userId
     */
    String getUserID();

    /**
     * Set the user id for the owner of these preferences
     *
     * @param userID
     */
    void setUserID(String userID);

    /**
     * Get job type preference of the user
     *
     * @return jobType
     */
    String getJobType();

    /**
     * Set job type preference of the user
     *
     * @param jobType
     */
    void setJobType(String jobType);

    /**
     * Get pay rate preference of the user
     *
     * @return
     */
    double getPayRate();

    /**
     * Set pay rate preference of the user
     *
     * @param payRate
     */
    void setPayRate(double payRate);

    /**
     * Get duration preference of the user
     *
     * @return duration
     */
    double getDuration();

    /**
     * Set duration preference of the user
     *
     * @param duration
     */
    void setDuration(double duration);
}

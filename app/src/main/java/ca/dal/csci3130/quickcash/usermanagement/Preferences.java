package ca.dal.csci3130.quickcash.usermanagement;

/**
 * Preference Object to save a user's preferences
 */
public class Preferences implements PreferenceInterface {

    private String userID;
    private String jobType;
    private double payRate;
    private double duration;

    // constructor
    public Preferences(String jobType, double payRate, double duration) {
        this.jobType = jobType;
        this.payRate = payRate;
        this.duration = duration;
    }

    // empty constructor
    public Preferences() {
    }

    // getters and setters for all data points

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String getJobType() {
        return jobType;
    }

    @Override
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public double getPayRate() {
        return payRate;
    }

    @Override
    public void setPayRate(double payRate) {
        this.payRate = payRate;
    }

    @Override
    public double getDuration() {
        return duration;
    }

    @Override
    public void setDuration(double duration) {
        this.duration = duration;
    }
}

package ca.dal.csci3130.quickcash.usermanagement;

public class Preferences implements PreferenceInterface {

    private String userID;
    private String jobType;
    private double payRate;
    private int duration;

    public Preferences(String jobType, double payRate, int duration) {
        this.jobType = jobType;
        this.payRate = payRate;
        this.duration = duration;
    }

    public Preferences(){}

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
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }
}

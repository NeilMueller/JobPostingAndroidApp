package ca.dal.csci3130.quickcash.usermanagement;

public interface PreferenceInterface {

    String getUserID();

    void setUserID(String userID);

    String getJobType();

    void setJobType(String jobType);

    double getPayRate();

    void setPayRate(double payRate);

    int getDuration();

    void setDuration(int duration);

}

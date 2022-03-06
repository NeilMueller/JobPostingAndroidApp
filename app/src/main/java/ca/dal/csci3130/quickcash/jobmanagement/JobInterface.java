package ca.dal.csci3130.quickcash.jobmanagement;

public interface JobInterface {
    String getJobTitle();

    void setJobTitle(String jobTitle);

    String getJobType();

    void setJobType(String jobType);

    String getDescription();

    void setDescription(String description);

    // duration of job in weeks
    int getDuration();

    void setDuration(int duration);

    int getPayRate();

    void setPayRate(int payRate);

    String getJobID();

    void setJobID(String jobID);

}

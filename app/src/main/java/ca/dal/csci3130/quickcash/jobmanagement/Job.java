package ca.dal.csci3130.quickcash.jobmanagement;

public class Job implements JobInterface{

    private String jobTitle;
    private String jobType;
    private String jobDescription;
    private int jobDuration;
    private int payRate;
    private String jobID;

    public Job(String jobTitle, String jobType, String jobDescription, int jobDuration, int payRate, String jobID) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.jobDescription = jobDescription;
        this.jobDuration = jobDuration;
        this.payRate = payRate;
        this.jobID = jobID;
    }

    public Job(){
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
    public String getDescription() {
        return jobDescription;
    }

    @Override
    public void setDescription(String description) {
        this.jobDescription = description;
    }

    @Override
    public int getDuration() {
        return jobDuration;
    }

    @Override
    public void setDuration(int duration) {
        this.jobDuration = duration;
    }

    @Override
    public int getPayRate() {
        return payRate;
    }

    @Override
    public void setPayRate(int payRate) {
        this.payRate = payRate;
    }

    @Override
    public String getJobID() {
        return jobID;
    }

    @Override
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }
}

package ca.dal.csci3130.quickcash.jobmanagement;

import java.util.ArrayList;

public class Job implements JobInterface{

    private String jobTitle;
    private String jobType;
    private String jobDescription;
    private String employerID;
    private int jobDuration;
    private double payRate;
    private String jobID;
    private double latitude, longitude;
    private ArrayList<String> applicants;
    private String selectedApplicant;

    public Job(String jobTitle, String jobType, String jobDescription, String employerID, int jobDuration, double payRate, String jobID, double latitude, double longitude,ArrayList<String> applicants,String selectedApplicant) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.jobDescription = jobDescription;
        this.employerID = employerID;
        this.jobDuration = jobDuration;
        this.payRate = payRate;
        this.jobID = jobID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.applicants = applicants;
        this.selectedApplicant = selectedApplicant;
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
    public double getPayRate() {
        return payRate;
    }

    @Override
    public void setPayRate(double payRate) {
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

    @Override
    public double getLatitude(){
        return this.latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public ArrayList<String> getApplicants() {
        return applicants;
    }

    @Override
    public void setApplicants(ArrayList<String> applicants) {
        this.applicants = applicants;
    }

    @Override
    public String getSelectedApplicant() {
        return selectedApplicant;
    }

    @Override
    public void setSelectedApplicant(String selectedApplicant) {
        this.selectedApplicant = selectedApplicant;
    }

    @Override
    public String getEmployerID() {
        return employerID;
    }

    @Override
    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    @Override
    public String getListedInfo(){

        String info = "Job Type: " + jobType;
        info = info + "\nDuration: " + String.valueOf(jobDuration) + " hrs";
        info = info + "\nPayrate: " + String.valueOf(payRate) + " $";
        info = info + "\nSelected Applicant: " + selectedApplicant;
        return info;
    }
}

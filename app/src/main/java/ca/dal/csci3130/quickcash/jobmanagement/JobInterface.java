package ca.dal.csci3130.quickcash.jobmanagement;

import java.util.List;

/**
 * Job object to save all the details of a given job
 */
public interface JobInterface {
    /**
     * Get Job Title
     *
     * @return jobTitle
     */
    String getJobTitle();

    /**
     * Set Job Title
     *
     * @param jobTitle
     */
    void setJobTitle(String jobTitle);

    /**
     * Get Job Type
     *
     * @return jobType
     */
    String getJobType();

    /**
     * Set JobType
     *
     * @param jobType
     */
    void setJobType(String jobType);

    /**
     * Get Job Description
     *
     * @return jobDescription
     */
    String getDescription();

    /**
     * Set Job Description
     *
     * @param description
     */
    void setDescription(String description);

    /**
     * Get Job Duration
     *
     * @return duration
     */
    int getDuration();

    /**
     * Set duration of the job
     *
     * @param duration
     */
    void setDuration(int duration);

    /**
     * Get Job pay rate
     *
     * @return payRate
     */
    double getPayRate();

    /**
     * set job pay rate
     *
     * @param payRate
     */
    void setPayRate(double payRate);

    /**
     * Get Job id
     *
     * @return jobId
     */
    String getJobID();

    /**
     * Set Job Id
     *
     * @param jobID
     */
    void setJobID(String jobID);

    /**
     * Get job location's latitude
     *
     * @return
     */
    double getLatitude();

    /**
     * Set job location's latitude
     *
     * @param latitude
     */
    void setLatitude(double latitude);

    /**
     * Get job location's longitude
     *
     * @return latitude
     */
    double getLongitude();

    /**
     * Set job location's longitude
     *
     * @param longitude
     */
    void setLongitude(double longitude);

    /**
     * Get all the applicants for this job
     *
     * @return List of applicants
     */
    List<String> getApplicants();

    /**
     * Set job applicants
     *
     * @param applicants
     */
    void setApplicants(List<String> applicants);

    /**
     * Get the selected applicant
     *
     * @return
     */
    String getSelectedApplicant();

    /**
     * Set selected applicant
     *
     * @param selectedApplicant
     */
    void setSelectedApplicant(String selectedApplicant);

    /**
     * Get the employer's Id who posted this job
     *
     * @return employerId
     */
    String getEmployerID();

    /**
     * Set Employer Id for this job
     *
     * @param employerID
     */
    void setEmployerID(String employerID);

    /**
     * Get the listed information for this job containing all job details
     *
     * @return jobInfo
     */
    String getListedInfo();

    /**
     * Get the status of the job: Open - true, Close - false
     *
     * @return jobStatus
     */
    boolean getJobStatusOpen();

    /**
     * Set job status
     *
     * @param jobStatusOpen
     */
    void setJobStatusOpen(boolean jobStatusOpen);

    /**
     * Checks whether this job is still accepting applications
     * @return true - if accepting applications else false
     */
    boolean acceptingApplications();
}

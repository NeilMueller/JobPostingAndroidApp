package ca.dal.csci3130.quickcash.jobmanagement;

import static org.junit.Assert.assertEquals;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobJUnitTest {

    @Test
    public void testGetInfo(){
        Map<String, String> jobData = new HashMap<>();
        jobData.put("jobTitle", "TITLE");
        jobData.put("jobType", "TYPE");
        jobData.put("jobDescription", "DESC");
        jobData.put("jobID", "JOBID");
        jobData.put("selectedApplicant", "");
        jobData.put("employerId", "EMPLOYERID");

        Job job = new Job(jobData, 10, 14.25, new LatLng(0,0), new ArrayList<>());
        String expected = "Job Type: TYPE" +
                "\nDuration: 10 hrs" +
                "\nPayrate: 14.25 $" +
                "\nSelected Applicant: " +
                "\nJob ID: JOBID";
        assertEquals(job.getListedInfo(), expected);
    }
}

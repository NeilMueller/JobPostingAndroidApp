package ca.dal.csci3130.quickcash.jobmanagement;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JobJUnitTest {

    @Test
    public void testGetInfo(){
        Job job = new Job("TITLE", "TYPE", "DESC", "EMPLOYERID", 10, 14.25, "JOBID", 0, 0);
        String expected = "Job Type: TYPE" +
                          "\nDuration: 10 hrs" +
                          "\nPayrate: 14.25 $" +
                          "\nSelected Applicant: " +
                          "\nJob ID: JOBID";
        assertEquals(job.getListedInfo(), expected);
    }
}

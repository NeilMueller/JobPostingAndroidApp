package ca.dal.csci3130.quickcash.jobmanagement;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class jobmanagementJUnitTest {

    @Mock
    static JobFormActivity job1 = Mockito.mock(JobFormActivity.class,Mockito.CALLS_REAL_METHODS);

    @Before
    public  void setup () {
        Mockito.doNothing().when(job1).addJob(any());
        Mockito.doNothing().when(job1).createToast(anyInt());
    }

    /** isEmpty() **/
    @Test
    public void emptyJobTitle() {

        assertTrue(job1.isEmpty("","full","read this",11,100));
    }

    @Test
    public void emptyJobType(){

        assertTrue(job1.isEmpty("job","","read",11,100));
    }

    @Test
    public void emptyJobDescription(){
        assertTrue(job1.isEmpty("job","full","",11,100));
    }

    @Test
    public void emptyDuration(){

        assertTrue(job1.isEmpty("job","full","read",0,100));
    }

    @Test
    public void emptyPayRate(){

        assertTrue(job1.isEmpty("job","full","read",11,0));
    }

    @Test
    public void validEntery(){
        assertFalse(job1.isEmpty("job","full","read",11,10));
    }
}

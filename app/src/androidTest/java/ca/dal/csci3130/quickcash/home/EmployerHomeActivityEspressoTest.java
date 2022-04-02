package ca.dal.csci3130.quickcash.home;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobFormActivity;
import ca.dal.csci3130.quickcash.jobmanagement.MyPostedJobsActivity;

public class EmployerHomeActivityEspressoTest {
    @Rule
    public ActivityTestRule<EmployerHomeActivity> activityTestRule =
            new ActivityTestRule<>(EmployerHomeActivity.class);

    @Before
    public void setup() { Intents.init(); }

    @Test
    public void testMoveToJobFormActivity() {
        onView(withId(R.id.job_Form)).perform(click());
        intended(hasComponent(JobFormActivity.class.getName()));
    }

    @Test
    public void testMoveToPostedJobsActivity() {
        onView(withId(R.id.btnMyPostedJobs)).perform(click());
        intended(hasComponent(MyPostedJobsActivity.class.getName()));
    }

    @After
    public void tearDown() { Intents.release(); }
}

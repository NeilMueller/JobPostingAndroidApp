package ca.dal.csci3130.quickcash.jobmanagement;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Intent;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;


public class AvailableJobsActivityEspressoTest {
    @Rule
    public ActivityTestRule<AvailableJobsActivity> activityTestRule =
            new ActivityTestRule<>(AvailableJobsActivity.class);

    @Before
    public void setup() { Intents.init(); }

    /*** test Return to Home link ***/
    @Test
    public void testMoveToAvailableJobsActivity() {
        onView(withId(R.id.btn_returnHome_employee)).perform(click());
        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    }


    @After
    public void tearDown() { Intents.release(); }

}

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
import ca.dal.csci3130.quickcash.jobmanagement.AppliedJobsActivity;
import ca.dal.csci3130.quickcash.jobmanagement.AvailableJobsActivity;

public class AvailableJobsActivityEspressoTest {

    @Rule
    public ActivityTestRule<AvailableJobsActivity> activityTestRule =
            new ActivityTestRule<>(AvailableJobsActivity.class);

    @Before
    public void setup() { Intents.init(); }

    /*** test Active Jobs link ***/
    @Test
    public void testMoveToEmployeeHome() {
        onView(withId(R.id.btn_returnHome_employee)).perform(click());
        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    }

    @After
    public void tearDown() { Intents.release(); }
}

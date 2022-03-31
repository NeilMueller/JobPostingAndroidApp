package ca.dal.csci3130.quickcash.jobmanagement;

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

import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

public class MyPostedJobsActivityEspressoTest {

    @Rule
    public ActivityTestRule<MyPostedJobsActivity> activityTestRule =
            new ActivityTestRule<>(MyPostedJobsActivity.class);

    @Before
    public void setup() { Intents.init(); }

    /*** test Active Jobs link ***/
    @Test
    public void testMoveToEmployerHome() {
        onView(withId(R.id.btnReturnEmployerHome)).perform(click());
        intended(hasComponent(EmployerHomeActivity.class.getName()));
    }

    @After
    public void tearDown() { Intents.release(); }
}

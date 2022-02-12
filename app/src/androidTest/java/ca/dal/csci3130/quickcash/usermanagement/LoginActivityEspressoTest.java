package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.ToastMatcher;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;

public class LoginActivityEspressoTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<EmployeeHomeActivity> activityTestRule1 =
            new ActivityTestRule<>(EmployeeHomeActivity.class);

    @Test
    public void Trylogin() {
        fillFields("jb123456@dal.ca", "Test123!");
        onView(withId(R.id.loginButton)).perform(click());
        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    }


    public void fillFields(String email, String password) {
        onView(withId(R.id.etEmailId)).perform(typeText(email));
        onView(withId(R.id.etPassword)).perform(typeText(password));
        Espresso.pressBack();
    }
}


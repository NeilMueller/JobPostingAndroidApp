package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.ToastMatcher;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

public class LoginActivityEspressoTest {

    @Before
    public void setup() {

        Intents.release();
        Intents.init();
    }

    @Rule
    public ActivityTestRule<LoginActivity> myIntentRule = new ActivityTestRule<>(LoginActivity.class);

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

    /**AT6:Given the employee is logged in successfully, the employer must be redirected to employer home page (needs to be employer user information)**/
    @Test
    public void checkIfEmployerPage() {
        onView(withId(R.id.etEmailId)).perform(clearText(), typeText("test@a.com"));
        onView(withId(R.id.etPassword)).perform(clearText(), typeText("Password!1"), closeSoftKeyboard());
        onView(withId(R.id.loginButtonCheckInfo)).perform(click());
        myIntentRule.launchActivity(new Intent());
        intended(hasComponent(EmployerHomeActivity.class.getName()));
    }
}


package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.ToastMatcher;

public class SignupActivityEspressoTest {

    @Rule
    public ActivityTestRule<SignupActivity> activityTestRule =
            new ActivityTestRule<>(SignupActivity.class);

    /*** isEmpty()**/
    @Test
    public void firstNameEmpty(){

        fillFields("", "Smith", "js123456@dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_missing_component)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    /*** isValidEmail()**/

    @Test
    public void isValidEmail(){
        fillFields("Joe", "Smith", "js123456@dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_email)).inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
    }

    @Test
    public void notValidEmail(){
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_email)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }


    /*** passwordLength()**/

    @Test
    public void shortPassword() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "A1e9fG!", "A1e9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_password)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void noUpperCase() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "abc1de9fg!", "abc1de9fg!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_password)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void noLowerCase() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "ABC1DE9FG!", "ABC1DE9FG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_password)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void noSpecialChar() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "Abc1de9fG", "Abc1de9fG");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_password)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void validPassword() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_password)).inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
    }

    /*** passwordConfirmation()**/

    @Test
    public void mismatch() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890","Abc1de9fG!", "Abc1de9FG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_password_mismatch)).inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
    }

    @Test
    public void passwordMatch() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_password_mismatch)).inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
    }

    /*** phoneLength()**/


    /***  demonstration() ONLY FOR DEMONSTRATION PURPOSES REMOVE**/
    @Test
    public void demonstration(){
        fillFields("", "", "", "", "", "");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_phone)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    /***  demonstration() ONLY FOR DEMONSTRATION PURPOSES REMOVE**/

    @Test
    public void shortPhone(){
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_phone)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void longPhone() {
        fillFields("Joe", "Smith", "js12345dal.ca", "Abc1de9fG!", "12345678909","Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_phone)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void validPhone() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890",  "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_phone)).inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
    }

    public void fillFields(String fName, String lName, String email, String phoneNum, String password, String cPassword){

        onView(withId(R.id.etFirstName)).perform(typeText(fName));
        onView(withId(R.id.etLastName)).perform(typeText(lName));
        onView(withId(R.id.etEmailIdSignUp)).perform(typeText(email));
        onView(withId(R.id.etPhoneNumber)).perform(typeText(phoneNum));
        onView(withId(R.id.etPasswordSignUp)).perform(typeText(password));
        onView(withId(R.id.etConfirmPasswordSignUp)).perform(typeText(cPassword));
        onView(withId(R.id.radioButtonEmployee)).perform(click());

    }



}

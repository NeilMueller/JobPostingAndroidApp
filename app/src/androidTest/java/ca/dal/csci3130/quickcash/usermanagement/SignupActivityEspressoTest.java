package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.ToastMatcher;
import ca.dal.csci3130.quickcash.common.Constants;

public class SignupActivityEspressoTest {

    DatabaseReference databaseReference;
    String userObjectKey;

    @Rule
    public ActivityTestRule<SignupActivity> activityTestRule =
            new ActivityTestRule<>(SignupActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    /*** isEmpty()**/
    @Test
    public void firstNameEmpty() {

        fillFields("", "Smith", "js123456@dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_missing_component)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    /*** isValidEmail()**/
    @Test
    public void notValidEmail() {
        fillFields("Joe", "Smith", "js12345dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_email)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }


    /*** passwordLength()**/

    @Test
    public void invalidPassword(){
        //short password
        checkToast("A1e9fG!");

        //no uppercase
        checkToast("abc1de9fg!");

        //no lowercase
        checkToast("ABC1DE9FG!");

        //no special char
        checkToast("Abc1de9fG");
    }

    /*** passwordConfirmation()**/

    @Test
    public void mismatch() {
        fillFields("Joe", "Smith", "js12345@dal.ca", "1234567890", "Abc1de9fG!", "Abc1de9FG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_password_mismatch)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    /*** phoneLength()**/

    @Test
    public void shortPhone() {
        fillFields("Joe", "Smith", "js12345@dal.ca", "123456789", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_phone)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void longPhone() {
        fillFields("Joe", "Smith", "js12345@dal.ca", "12345678909", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_phone)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void CheckIfUserExists() throws InterruptedException {
        addUserToDB(new User("Joe", "Smith", "test@a.com", "1234567890", "Abc1de9fG!", true));

        fillFields("Joe", "Smith", "test@a.com", "1234567890", "Abc1de9fG!", "Abc1de9fG!");
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText("email already exists please login")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // remove the added user from the db to avoid clutter
        databaseReference.child(userObjectKey).removeValue();
    }

    private void checkToast(String password){
        fillFields("Joe", "Smith", "js12345@dal.ca", "1234567890", password, password);
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.toast_invalid_password)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        emptyFields();
    }

    private void emptyFields(){
        onView(withId(R.id.etFirstName)).perform(clearText());
        onView(withId(R.id.etLastName)).perform(clearText());
        onView(withId(R.id.etEmailIdSignUp)).perform(clearText());
        onView(withId(R.id.etPhoneNumber)).perform(clearText());
        onView(withId(R.id.etPasswordSignUp)).perform(clearText());
        onView(withId(R.id.etConfirmPasswordSignUp)).perform(clearText(), closeSoftKeyboard());
    }

    public void fillFields(String fName, String lName, String email, String phoneNum, String password, String cPassword) {
        onView(withId(R.id.etFirstName)).perform(typeText(fName));
        onView(withId(R.id.etLastName)).perform(typeText(lName));
        onView(withId(R.id.etEmailIdSignUp)).perform(typeText(email));
        onView(withId(R.id.etPhoneNumber)).perform(typeText(phoneNum));
        onView(withId(R.id.etPasswordSignUp)).perform(typeText(password));
        onView(withId(R.id.etConfirmPasswordSignUp)).perform(typeText(cPassword), closeSoftKeyboard());
    }

    public void addUserToDB(UserInterface user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        databaseReference = db.getReference(User.class.getSimpleName());

        userObjectKey = databaseReference.push().getKey();
        if (userObjectKey == null)
            throw new NullPointerException("User Object Key is null!");
        databaseReference.child(userObjectKey).setValue(user);
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}

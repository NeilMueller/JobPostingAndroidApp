package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.AppliedJobsActivity;
import ca.dal.csci3130.quickcash.jobmanagement.AvailableJobsActivity;
import ca.dal.csci3130.quickcash.jobmanagement.FeedbackDAO;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAO;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAOAdapter;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.UserDAOAdapter;

public class EmployeeHomeActivity extends AppCompatActivity {

    private TextView ratingTV;
    private TextView numOfRater;
    private SessionManagerInterface sessionManager;
    DAO dao;

    @Override
    public void onBackPressed () {
        //Prevent user from using back button once logged in
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        Button availableJobs = findViewById(R.id.btn_seeAvailableJobs);
        Button preferencesButton = findViewById(R.id.buttonToPref);
        Button appliedJobsButton = findViewById(R.id.btn_Applied_Jobs);
        Button logoutButton = findViewById(R.id.btn_logout_employee);
        ratingTV = findViewById(R.id.ratingTV);
        numOfRater = findViewById(R.id.numOfRaterTV);
        dao = new UserDAOAdapter(new UserDAO());

        sessionManager = SessionManager.getSessionManager(getApplicationContext());
        //Gets the name from the session
        String fullName = sessionManager.getKeyName();
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");

        displayRating();

        // printing welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeEmployee);
        welcomeMessage.setText(String.format("Welcome Employee, %s", fullName));

        logoutButton.setOnClickListener(view -> logout());
        availableJobs.setOnClickListener(view -> moveToAvailableJobsActivity());
        preferencesButton.setOnClickListener(view -> moveToPreferenceActivity());
        appliedJobsButton.setOnClickListener(view -> moveToAppliedJobsActivity());
    }

    protected void displayRating () {
        String email = sessionManager.getKeyEmail();

        DatabaseReference ref = dao.getDatabaseReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && email.equals(user.getEmail())){
                        ratingTV.setText("" + String.format("%.2f", user.getRating()) + "/5");
                        numOfRater.setText("" + user.getNumberOfRatings());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Deletes session and opens login screen
     *
     */
    public void logout() {

        SessionManagerInterface session = SessionManager.getSessionManager(EmployeeHomeActivity.this);
        session.logoutUser();

        moveToLoginActivity();
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(EmployeeHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void moveToAvailableJobsActivity() {
        Intent intent = new Intent(EmployeeHomeActivity.this, AvailableJobsActivity.class);
        startActivity(intent);
    }

    private void moveToPreferenceActivity() {
        Intent intent = new Intent(EmployeeHomeActivity.this, PreferenceActivity.class);
        startActivity(intent);
    }

    private void moveToAppliedJobsActivity(){
        Intent intent = new Intent(EmployeeHomeActivity.this, AppliedJobsActivity.class);
        startActivity(intent);
    }

}
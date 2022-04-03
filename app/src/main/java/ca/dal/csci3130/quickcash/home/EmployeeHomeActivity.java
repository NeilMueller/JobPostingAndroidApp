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
import ca.dal.csci3130.quickcash.common.AbstractDAO;
import ca.dal.csci3130.quickcash.jobmanagement.AppliedJobsActivity;
import ca.dal.csci3130.quickcash.jobmanagement.AvailableJobsActivity;
import ca.dal.csci3130.quickcash.jobmanagement.FeedbackDAO;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;

public class EmployeeHomeActivity extends AppCompatActivity {

    private Button availableJobs;
    private Button preferencesButton;
    private Button appliedJobsButton;
    private TextView ratingTV;
    private TextView numOfRater;

    @Override
    //Prevent user from using back button once logged in
    public void onBackPressed () {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        availableJobs = findViewById(R.id.btn_seeAvailableJobs);
        preferencesButton = findViewById(R.id.buttonToPref);
        appliedJobsButton = findViewById(R.id.btn_Applied_Jobs);
        ratingTV = findViewById(R.id.ratingTV);
        numOfRater = findViewById(R.id.numOfRaterTV);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        //Gets the name from the session
        String fullName = sessionManager.getKeyName();
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");

        displayRating();

        // printing welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeEmployee);
        welcomeMessage.setText(String.format("Welcome Employee, %s", fullName));

        availableJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAvailableJobsActivity();
            }
        });

        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToPreferenceActivity();
            }
        });

        appliedJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAppliedJobsActivity();
            }
        });


    }

    protected void displayRating () {


        SessionManager sessionManager1 = new SessionManager(getApplicationContext());
        String email = sessionManager1.getKeyEmail();

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
        AbstractDAO userDAO = new UserDAO();
        DatabaseReference ref = userDAO.getDatabaseReference();
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
     * @param view
     */

    public void logout(View view) {
        SessionManager session = new SessionManager(EmployeeHomeActivity.this);
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
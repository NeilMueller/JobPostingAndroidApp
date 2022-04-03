package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobFormActivity;
import ca.dal.csci3130.quickcash.jobmanagement.MyPostedJobsActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserDAOAdapter;

public class EmployerHomeActivity extends AppCompatActivity {

    private TextView ratingTV;
    private TextView numOfRater;
    SessionManagerInterface sessionManager;
    DAO dao;

    @Override
    public void onBackPressed () {
        //Prevent user from using back button once logged in
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);
        Button jobFormButton = findViewById(R.id.job_Form);
        Button logoutButton = findViewById(R.id.btn_logout_employer);
        Button myJobsButton = findViewById(R.id.btnMyPostedJobs);
        dao = new UserDAOAdapter(new UserDAO());

        ratingTV = findViewById(R.id.ratingTV);
        numOfRater = findViewById(R.id.numOfRaterTV);

        displayRating();

        sessionManager = SessionManager.getSessionManager(getApplicationContext());
        //Gets the name from the session
        String fullName = sessionManager.getKeyName();
        // printing welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeEmployer);
        welcomeMessage.setText(String.format("Welcome Employer, %s", fullName));
        FirebaseMessaging.getInstance().unsubscribeFromTopic("jobs");


        logoutButton.setOnClickListener(view -> logout());
        jobFormButton.setOnClickListener(view -> moveToJobFormActivity());
        myJobsButton.setOnClickListener(view -> moveToMyJobsActivity());
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
     * Logout the user and move to login
     */
    public void logout() {
        SessionManagerInterface session = SessionManager.getSessionManager(EmployerHomeActivity.this);
        session.logoutUser();

        moveToLoginActivity();
    }

    /**
     * Shift to login page
     */
    private void moveToLoginActivity() {
        Intent intent = new Intent(EmployerHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void moveToJobFormActivity() {
        Intent intent = new Intent(getApplicationContext(), JobFormActivity.class);
        startActivity(intent);
    }

    private void moveToMyJobsActivity() {
        Intent intent = new Intent(getApplicationContext(), MyPostedJobsActivity.class);
        startActivity(intent);
    }
}
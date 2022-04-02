package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.AvailableJobsActivity;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;

public class EmployeeHomeActivity extends AppCompatActivity {

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
        Button logoutButton = findViewById(R.id.btn_logout_employee);

        SessionManagerInterface sessionManager = SessionManager.getSessionManager(getApplicationContext());
        //Gets the name from the session
        String fullName = sessionManager.getKeyName();

        // printing welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeEmployee);
        welcomeMessage.setText(String.format("Welcome Employee, %s", fullName));

        logoutButton.setOnClickListener(view -> logout());
        availableJobs.setOnClickListener(view -> moveToAvailableJobsActivity());
        preferencesButton.setOnClickListener(view -> moveToPreferenceActivity());
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

}
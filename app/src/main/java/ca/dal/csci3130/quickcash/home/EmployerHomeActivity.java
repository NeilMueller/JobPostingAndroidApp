package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobFormActivity;
import ca.dal.csci3130.quickcash.jobmanagement.MyPostedJobsActivity;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class EmployerHomeActivity extends AppCompatActivity {

    private Button makePaymentButton;
    private Button jobFormButton;
    private Button myJobsButton;
    /**
     * Prevent user from using back button once logged in
     */
    @Override
    public void onBackPressed () {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);
        makePaymentButton = findViewById(R.id.btnMakePayment);
        jobFormButton = findViewById(R.id.job_Form);
        myJobsButton = findViewById(R.id.btnMyPostedJobs);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        //Gets the name from the session
        String fullName = sessionManager.getKeyName();
        // printing welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeEmployer);
        welcomeMessage.setText(String.format("Welcome Employer, %s", fullName));
        FirebaseMessaging.getInstance().unsubscribeFromTopic("jobs");


        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPayPalPaymentActivity();
            }
        });

        jobFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToJobFormActivity();
            }
        });

        myJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMyJobsActivity();
            }
        });
    }

    /**
     * Logout the user and move to login
     * @param view
     */
    public void logout(View view) {
        SessionManager session = new SessionManager(EmployerHomeActivity.this);
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

    private void moveToPayPalPaymentActivity() {
        Intent intent = new Intent(EmployerHomeActivity.this, PayPalPaymentActivity.class);
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
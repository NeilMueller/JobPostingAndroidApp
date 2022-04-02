package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobFormActivity;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;

public class EmployerHomeActivity extends AppCompatActivity {


    @Override
    public void onBackPressed () {
        //Prevent user from using back button once logged in
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);
        Button makePaymentButton = findViewById(R.id.btnMakePayment);
        Button jobFormButton = findViewById(R.id.job_Form);
        Button logoutButton = findViewById(R.id.btn_logout_employer);

        SessionManagerInterface sessionManager = SessionManager.getSessionManager(getApplicationContext());
        //Gets the name from the session
        String fullName = sessionManager.getKeyName();
        // printing welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeEmployer);
        welcomeMessage.setText(String.format("Welcome Employer, %s", fullName));


        logoutButton.setOnClickListener(view -> logout());
        makePaymentButton.setOnClickListener(v -> moveToPayPalPaymentActivity());
        jobFormButton.setOnClickListener(view -> moveToJobFormActivity());
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

    private void moveToPayPalPaymentActivity() {
        Intent intent = new Intent(EmployerHomeActivity.this, PayPalPaymentActivity.class);
        startActivity(intent);
    }

    private void moveToJobFormActivity() {
        Intent intent = new Intent(getApplicationContext(), JobFormActivity.class);
        startActivity(intent);
    }
}
package ca.dal.csci3130.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.SignupActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * Called at the very start to check if a user is already logged in
     */
    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add logic to handle the two buttons added in the UI

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        Button signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Checks if session already exists and moves user to home page session exists
     */
    private void checkSession() {
        SessionManagerInterface session = SessionManager.getSessionManager(getApplicationContext());
        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn) {
            boolean isEmployee = session.getIsEmployee();
            if (isEmployee) {
                moveToEmployeePage();
            } else {
                moveToEmployerPage();
            }
        }
    }

    /**
     * Move to employee home page
     */
    private void moveToEmployeePage() {
        Intent intentEmployee = new Intent(MainActivity.this, EmployeeHomeActivity.class);
        intentEmployee.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentEmployee);
    }

    /**
     * Move to employer home page
     */
    private void moveToEmployerPage() {
        Intent intentEmployer = new Intent(MainActivity.this, EmployerHomeActivity.class);
        intentEmployer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentEmployer);
    }
}
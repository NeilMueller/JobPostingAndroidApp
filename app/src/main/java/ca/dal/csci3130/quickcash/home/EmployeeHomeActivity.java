package ca.dal.csci3130.quickcash.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;

public class EmployeeHomeActivity extends AppCompatActivity {

    @Override
    //Prevent user from using back button once logged in
    public void onBackPressed () {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
    }
}
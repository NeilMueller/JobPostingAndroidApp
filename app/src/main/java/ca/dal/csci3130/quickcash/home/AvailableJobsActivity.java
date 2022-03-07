package ca.dal.csci3130.quickcash.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.dal.csci3130.quickcash.R;

public class AvailableJobsActivity extends AppCompatActivity {

    private Button returnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_jobs);
        returnHome = findViewById(R.id.btn_returnHome_employee);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvailableJobsActivity.this, EmployeeHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
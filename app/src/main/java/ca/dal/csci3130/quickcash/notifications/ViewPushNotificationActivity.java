package ca.dal.csci3130.quickcash.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.jobmanagement.AvailableJobsActivity;

public class ViewPushNotificationActivity extends AppCompatActivity {

    private TextView titleTV;
    private TextView bodyTV;
    private TextView jobIdTV;
    private TextView jobLocationTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_push_notification);
        init();
        setData();
        Button availableJobs = findViewById(R.id.btn_ToAvailable_Jobs);

        availableJobs.setOnClickListener(view -> moveToAvailableJobsActivity());
    }

    private void init() {
        titleTV = findViewById(R.id.titleTV);
        bodyTV = findViewById(R.id.bodyTV);
        jobIdTV = findViewById(R.id.jobIdTV);
        jobLocationTV = findViewById(R.id.jobLocationTV);
    }

    private void setData() {
        final Bundle extras = getIntent().getExtras();
        final String title = extras.getString("title");
        final String body = extras.getString("body");
        final String jobId = extras.getString("jobId");
        final String jobLocation = extras.getString("jobLocation");

        titleTV.setText(title);
        bodyTV.setText(body);
        jobIdTV.setText(jobId);
        jobLocationTV.setText(jobLocation);
    }

    private void moveToAvailableJobsActivity() {
        Intent intent = new Intent(ViewPushNotificationActivity.this, AvailableJobsActivity.class);
        startActivity(intent);
    }
}
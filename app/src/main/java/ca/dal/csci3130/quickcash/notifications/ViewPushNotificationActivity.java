package ca.dal.csci3130.quickcash.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.AvailableJobsActivity;

public class ViewPushNotificationActivity extends AppCompatActivity {

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_push_notification);

        TextView titleTV = findViewById(R.id.titleTV);
        TextView bodyTV = findViewById(R.id.bodyTV);
        TextView jobIdTV = findViewById(R.id.jobIdTV);
        TextView jobLocationTV = findViewById(R.id.jobLocationTV);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String title = extras.getString("title");
            String body = extras.getString("body");
            String jobId = extras.getString("jobId");
            String jobLocation = extras.getString("jobLocation");

            titleTV.setText(title);
            bodyTV.setText(body);
            jobIdTV.setText(jobId);
            jobLocationTV.setText(jobLocation);
        }

        Button availableJobs = findViewById(R.id.btn_ToAvailable_Jobs);
        availableJobs.setOnClickListener(view -> moveToAvailableJobsActivity());
    }

    /**
     * Move to available jobs
     */
    private void moveToAvailableJobsActivity() {
        Intent intent = new Intent(ViewPushNotificationActivity.this, AvailableJobsActivity.class);
        startActivity(intent);
    }
}
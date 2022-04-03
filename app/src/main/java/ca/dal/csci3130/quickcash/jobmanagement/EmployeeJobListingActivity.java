package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;

public class EmployeeJobListingActivity extends AppCompatActivity {

    String jobID;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
    private TextView employer;
    private TextView status;
    private Button rateBtn;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_job_listing);

        jobTitle = findViewById(R.id.jobAdTitle_eja);
        jobDesc = findViewById(R.id.jobAdDescription_eja);
        jobType = findViewById(R.id.jobAdType_eja);
        jobDuration = findViewById(R.id.jobAdDuration_eja);
        jobPayRate = findViewById(R.id.jobAdPayRate_eja);
        employer = findViewById(R.id.tv_employer);
        rateBtn = findViewById(R.id.btn_rate_employer);
        status = findViewById(R.id.applicationStatusTextView);
        userEmail = grabEmail();

        // Grab job id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobID = extras.getString("JobID").trim();
        }

        fillFields();
        rateBtn.setEnabled(false);
        rateBtn.setOnClickListener(v -> moveToFeedBackActivity());

    }

    private void fillFields() {
        // query the database and find the job by its ID
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if (newJob != null && newJob.getJobID().matches(jobID)) {
                        jobTitle.setText(newJob.getJobTitle());
                        jobDesc.setText(newJob.getDescription());
                        jobType.setText(newJob.getJobType());
                        jobDuration.setText(String.valueOf(newJob.getDuration()));
                        jobPayRate.setText(String.valueOf(newJob.getPayRate()));
                        employer.setText(newJob.getEmployerID());

                        if(newJob.acceptingApplications()){
                            status.setText("Application In Progress");
                        }
                        else if(newJob.getSelectedApplicant().equals(userEmail)){
                            if(newJob.getJobStatusOpen()) {
                                status.setText("SELECTED");
                            }
                            else {
                                status.setText("CLOSED");
                                Toast.makeText(getApplicationContext(), "Job Is Completed and Closed", Toast.LENGTH_LONG).show();
                            }
                            rateBtn.setEnabled(true);
                        }
                        else{
                            status.setText("Another Candidate Selected");
                            rateBtn.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - fillFields (EmployeeJobListing)", error.getMessage());
            }
        });
    }

    /**
     * Returns the email of the user signed in
     * @return
     */

    private String grabEmail() {

        SessionManagerInterface session = SessionManager.getSessionManager(this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn){
            return  session.getKeyEmail();
        }
        return null;
    }

    private void moveToFeedBackActivity(){
        Intent intent = new Intent(EmployeeJobListingActivity.this, FeedbackActivity.class);
        intent.putExtra("userID", employer.getText());
        startActivity(intent);
    }
}
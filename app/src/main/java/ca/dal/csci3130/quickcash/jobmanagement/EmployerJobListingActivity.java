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
import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.jobmanagement.FeedbackActivity;

public class EmployerJobListingActivity extends AppCompatActivity {

    String jobID;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
    private TextView candidate;
    private TextView status;
    private Button paymentBtn;
    private Button rateEmployeeBtn;
    private ArrayList<String> applicants;
    private ListView applicantListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_job_listing);

        jobTitle = findViewById(R.id.jobAdTitle_emp);
        jobDesc = findViewById(R.id.jobAdDescription);
        jobType = findViewById(R.id.jobAdType_emp);
        jobDuration = findViewById(R.id.jobAdDuration);
        jobPayRate = findViewById(R.id.jobAdPayRate);
        status = findViewById(R.id.tv_status_display);
        applicantListView = findViewById(R.id.list_empJobListing);
        candidate = findViewById(R.id.tv_selected_candidate);
        paymentBtn = findViewById(R.id.btn_payEmployee);
        paymentBtn.setEnabled(false);
        rateEmployeeBtn = findViewById(R.id.btn_rate_employee);
        rateEmployeeBtn.setEnabled(false);

        // Grab job id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobID = extras.getString("JobID").trim();
        }

        fillFields();

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeJobStatus();
                moveToPayPalPaymentActivity();
            }
        });

        rateEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToFeedbackActivity();
            }
        });

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
                        jobTitle.setText("" + newJob.getJobTitle());
                        jobDesc.setText("" + newJob.getDescription());
                        jobType.setText("" + newJob.getJobType());
                        jobDuration.setText("" + newJob.getDuration());
                        jobPayRate.setText("" + newJob.getPayRate());
                        candidate.setText("" + newJob.getSelectedApplicant());
                        applicants = newJob.getApplicants();
                        showApplicants(applicants);
                        //Set JobStatus Filed
                        if (newJob.getJobStatusOpen()) {
                            status.setText("Open");
                        } else {
                            status.setText("CLOSED");
                        }
                        //Enable/Disable Payment Button
                        if(newJob.getSelectedApplicant().contains("@")) {
                            paymentBtn.setEnabled(true);
                            rateEmployeeBtn.setEnabled(true);
                        }
                        if(!newJob.getJobStatusOpen()) {
                            paymentBtn.setEnabled(false);
                            rateEmployeeBtn.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showApplicants(ArrayList<String> applicants) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EmployerJobListingActivity.this,
                android.R.layout.simple_list_item_1, applicants);
        applicantListView.setAdapter(adapter);
        makeApplicantsClickable(adapter);
    }

    public void makeApplicantsClickable(ArrayAdapter adapter) {
        applicantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // after the employer clicks on the applicant, take them to an activity showing
                // info about the applicant, with a button showing to accept them for the job

                // first, find the user by their email.
                String userEmail = adapter.getItem(i).toString().trim();
//                Toast.makeText(getApplicationContext(), userEmail, Toast.LENGTH_LONG).show();
                showApplicantInfo(userEmail);
//                String employeeName = user.getFirstName() + " " + user.getLastName();

            }
        });
    }

    public void showApplicantInfo(String userEmail) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
        final User[] user = {null};
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    user[0] = snapshot1.getValue(User.class);
                    if(user[0] != null && user[0].getEmail().matches(userEmail)) {
                        // after we have found the user, start the applicant info intent
                        String employeeName = user[0].getFirstName() + " " + user[0].getLastName();
                        Intent intent = new Intent(getApplicationContext(),
                                ApplicantInfoActivity.class);
                        intent.putExtra("EmpName", employeeName);
                        intent.putExtra("EmpEmail", user[0].getEmail());
                        intent.putExtra("JobID", jobID);
                        intent.putExtra("Rating", user[0].getRating());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void moveToPayPalPaymentActivity() {
        Intent intent = new Intent(EmployerJobListingActivity.this, PayPalPaymentActivity.class);
        startActivity(intent);
    }

    private void closeJobStatus() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if (newJob != null && newJob.getJobID().matches(jobID)) {
                        DatabaseReference jobRef = snapshot1.getRef();
                        Map<String, Object> jobUpdate = new HashMap<>();
                        jobUpdate.put("jobStatusOpen", false);
                        jobRef.updateChildren(jobUpdate);
                        Toast.makeText(getApplicationContext(), "Job Closed, Moving to Payment",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void moveToFeedbackActivity() {
        Intent intent = new Intent(EmployerJobListingActivity.this, FeedbackActivity.class);
        intent.putExtra("userID", candidate.getText().toString());
        Log.d("userID", candidate.getText().toString());
        startActivity(intent);
    }
}
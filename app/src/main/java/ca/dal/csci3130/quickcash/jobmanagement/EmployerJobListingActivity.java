package ca.dal.csci3130.quickcash.jobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.paymentmanagement.PayPalPaymentActivity;
import ca.dal.csci3130.quickcash.usermanagement.User;

public class EmployerJobListingActivity extends AppCompatActivity {

    private String jobID;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
    private TextView candidate;
    private TextView status;
    private Button paymentBtn;
    private Button rateEmployeeBtn;
    private List<String> applicants;
    private ListView applicantListView;
    private DAO dao;
    private double totalPay;

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_job_listing);

        dao = new JobDAOAdapter(new JobDAO());

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
        totalPay = 0;

        // Grab job id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobID = extras.getString("JobID").trim();
        }

        fillFields();

        paymentBtn.setOnClickListener(v -> moveToPayPalPaymentActivity());
        rateEmployeeBtn.setOnClickListener(view -> moveToFeedbackActivity());
    }

    /**
     * Get the requested job from db and fill fields in the UI
     */
    private void fillFields() {
        // query the database and find the job by its ID
        DatabaseReference ref = dao.getDatabaseReference();
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
                        candidate.setText(newJob.getSelectedApplicant());
                        applicants = newJob.getApplicants();
                        totalPay = newJob.getPayRate() * newJob.getDuration();
                        showApplicants(applicants);

                        if (newJob.acceptingApplications()) {
                            status.setText(R.string.open);
                            rateEmployeeBtn.setEnabled(false);
                            paymentBtn.setEnabled(false);
                        } else {
                            status.setText(R.string.candidate_selected);
                            paymentBtn.setEnabled(true);
                        }

                        if (!newJob.getJobStatusOpen()) {
                            status.setText(R.string.closed);
                            rateEmployeeBtn.setEnabled(true);
                            paymentBtn.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - fillFields (EmployerJobListingActivity)", error.getMessage());
            }
        });
    }

    /**
     * Show Applicants in the UI
     *
     * @param applicants
     */
    public void showApplicants(List<String> applicants) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EmployerJobListingActivity.this,
                android.R.layout.simple_list_item_1, applicants);
        applicantListView.setAdapter(adapter);
        makeApplicantsClickable(adapter);
    }

    /**
     * Clicking on applicants should open their profile - rating and email
     *
     * @param adapter
     */
    public void makeApplicantsClickable(ArrayAdapter<String> adapter) {
        applicantListView.setOnItemClickListener((adapterView, view, i, l) -> {
            // after the employer clicks on the applicant, take them to an activity showing
            // info about the applicant, with a button showing to accept them for the job

            // first, find the user by their email.
            String userEmail = adapter.getItem(i).trim();
            showApplicantInfo(userEmail);
        });
    }

    /**
     * Get the applicant's details from the database
     *
     * @param userEmail
     */
    public void showApplicantInfo(String userEmail) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if (user != null && user.getEmail().matches(userEmail)) {
                        // after we have found the user, start the applicant info intent
                        String employeeName = user.getFirstName() + " " + user.getLastName();
                        Intent intent = new Intent(EmployerJobListingActivity.this, ApplicantInfoActivity.class);
                        intent.putExtra("EmpName", employeeName);
                        intent.putExtra("EmpEmail", user.getEmail());
                        intent.putExtra("JobID", jobID);
                        intent.putExtra("Rating", user.getRating());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - showApplicantInfo (EmployerJobListingActivity)", error.getMessage());
            }
        });
    }

    /**
     * Move to paypal activity
     */
    private void moveToPayPalPaymentActivity() {
        Intent intent = new Intent(EmployerJobListingActivity.this, PayPalPaymentActivity.class);
        intent.putExtra("jobId", jobID);
        if(totalPay != 0){
            intent.putExtra("totalPay", totalPay);
        }
        startActivity(intent);
    }

    /**
     * Move to Feedback Activity
     */
    private void moveToFeedbackActivity() {
        Intent intent = new Intent(EmployerJobListingActivity.this, FeedbackActivity.class);
        intent.putExtra("userID", candidate.getText().toString());
        Log.d("userID", candidate.getText().toString());
        startActivity(intent);
    }
}
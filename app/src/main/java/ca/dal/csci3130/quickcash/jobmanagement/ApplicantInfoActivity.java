package ca.dal.csci3130.quickcash.jobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;

public class ApplicantInfoActivity extends AppCompatActivity {

    private String empEmail;
    private String jobID;
    private String finalJobID;
    private String finalEmpEmail;
    private double rating;
    private String empName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_info);

        Button acceptApplicantButton = findViewById(R.id.btn_acceptApplicant);

        fillFields();

        // when we accept an applicant, the applicant should appear as the jobs "selected applicant"
        // and the job should not accept any more applications
        finalJobID = jobID.trim();
        finalEmpEmail = empEmail;

        acceptApplicantButton.setOnClickListener(view -> acceptCandidate());
    }

    private void acceptCandidate() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if (newJob != null && newJob.getJobID().matches(finalJobID)) {
                        DatabaseReference jobRef = snapshot1.getRef();
                        Map<String, Object> jobUpdate = new HashMap<>();
                        jobUpdate.put("selectedApplicant", finalEmpEmail);
                        jobUpdate.put("applicants", new ArrayList<>());
                        jobRef.updateChildren(jobUpdate);
                        createToast(R.string.candidate_accepted);
                        moveToJobListing();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - acceptCandidate (ApplicantInfo):", error.getMessage());
            }
        });
    }

    private void fillFields() {
        TextView empNameTextView = findViewById(R.id.tv_applicantName);
        TextView empEmailTextView = findViewById(R.id.tv_applicantEmail);
        RatingBar ratingRatingBar = findViewById(R.id.ratingBarApplicant);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            empName = extras.getString("EmpName");
            empEmail = extras.getString("EmpEmail");
            jobID = extras.getString("JobID");
            rating = extras.getDouble("Rating");
        }
        empNameTextView.setText(empName);
        empEmailTextView.setText(empEmail);
        ratingRatingBar.setRating((float) rating);
    }

    private void moveToJobListing() {
        Intent intent = new Intent(this, EmployerJobListingActivity.class);
        intent.putExtra("JobID", jobID);
        startActivity(intent);
    }

    /**
     * method to create Toast message upon error
     *
     * @param messageId
     */
    protected void createToast(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_LONG).show();
    }
}
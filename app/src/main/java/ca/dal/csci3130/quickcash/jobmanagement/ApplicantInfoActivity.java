package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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

public class ApplicantInfoActivity extends AppCompatActivity {

    private String empName;
    String empEmail;
    String JobID;
    String finalJobID;
    String finalEmpEmail;
    double rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_info);

        Button acceptApplicantButton = findViewById(R.id.btn_acceptApplicant);

        fillFields();

        // when we accept an applicant, the applicant should appear as the jobs "selected applicant"
        // and the job should not accept any more applications
        finalJobID = JobID.trim();
        finalEmpEmail = empEmail;

        acceptApplicantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptCandidate();
            }
        });
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
                        ArrayList<String> emptyArrayList = new ArrayList<>();
                        emptyArrayList.add("");
                        jobUpdate.put("applicants", emptyArrayList);
                        jobRef.updateChildren(jobUpdate);
                        Toast.makeText(getApplicationContext(), "Candidate accepted",
                                Toast.LENGTH_LONG).show();
                        moveToJobListing();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            JobID = extras.getString("JobID");
            rating = extras.getDouble("Rating");
        }
        empNameTextView.setText(empName);
        empEmailTextView.setText(empEmail);
        ratingRatingBar.setRating((float) rating);
    }

    private void moveToJobListing(){
        Intent intent = new Intent(getApplicationContext(), EmployerJobListingActivity.class);
        intent.putExtra("JobID", JobID);
        startActivity(intent);
    }
}
package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;

public class EmployerJobListingActivity extends AppCompatActivity {

    String jobID;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
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
        applicantListView = findViewById(R.id.list_empJobListing);

        ArrayAdapter adapter = new ArrayAdapter(this.getApplicationContext(),
                android.R.layout.simple_list_item_1);



        // Grab job id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobID = extras.getString("JobID").trim();
        }

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
                        applicants = newJob.getApplicants();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        applicantListView.setAdapter(adapter);

    }
}
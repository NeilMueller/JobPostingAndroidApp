package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.User;

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
                        showApplicants(applicants);
//                        makeApplicantsClickable(applicants);
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
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
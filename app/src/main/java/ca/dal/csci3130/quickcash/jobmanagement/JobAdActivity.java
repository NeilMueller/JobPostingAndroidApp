package ca.dal.csci3130.quickcash.jobmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class JobAdActivity extends AppCompatActivity {

    String jobID;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
    private Button apply;


    public JobAdActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_ad);

        jobTitle = findViewById(R.id.jobAdTitle);
        jobDesc = findViewById(R.id.jobAdDescription);
        jobType = findViewById(R.id.jobAdType);
        jobDuration = findViewById(R.id.jobAdDuration);
        jobPayRate = findViewById(R.id.jobAdPayRate);

        // Grab job id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobID = extras.getString("JobID");
        }


        // query the database and find the job by its ID
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if(newJob != null && newJob.getJobID().matches(jobID)) {
                        jobTitle.setText("" + newJob.getJobTitle());
                        jobDesc.setText("" + newJob.getDescription());
                        jobType.setText("" + newJob.getJobType());
                        jobDuration.setText("" + newJob.getDuration());
                        jobPayRate.setText("" + newJob.getPayRate());
                        if (!newJob.acceptingApplications()) {
                            String buttonText = "SELECTED";
                            if(!newJob.getSelectedApplicant().equals(grabEmail())){
                                buttonText = "ANOTHER CANDIDATE SELECTED";
                            }
                            apply.setText(buttonText);
                            apply.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        apply = (Button) findViewById(R.id.apply);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Job newJob = snapshot1.getValue(Job.class);
                            if (newJob != null && newJob.getJobID().matches(jobID)) {
                                ArrayList<String> applicants = newJob.getApplicants();
                                if (applicants != null && applicants.contains(grabEmail())) {
                                    createToast(R.string.already_applied);
                                } else {
                                    if (applicants == null)
                                        applicants = new ArrayList<String>();

                                    DatabaseReference newJobPref = snapshot1.getRef();
                                    applicants.add(grabEmail());
                                    Map<String, Object> newJobUpdate = new HashMap<>();
                                    newJobUpdate.put("applicants", applicants);
                                    newJobPref.updateChildren(newJobUpdate);
                                    createToast(R.string.applied);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    /**
     * Returns the email of the user signed in
     * @return
     */
    private String grabEmail(){
        SessionManager session = new SessionManager(JobAdActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if(isLoggedIn)
            return session.getKeyEmail();

       return null;
    }

    /**
     * method to create Toast message upon error
     * @param messageId
     */
    protected void createToast(int messageId){
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

}
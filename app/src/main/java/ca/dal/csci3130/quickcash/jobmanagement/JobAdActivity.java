package ca.dal.csci3130.quickcash.jobmanagement;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserDAOAdapter;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class JobAdActivity extends AppCompatActivity {

    String jobID;
    private DAO dao;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
    private Button apply;


    public JobAdActivity() {
        // empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_ad);

        dao = new JobDAOAdapter(new JobDAO());

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

        findJob();

        apply = (Button) findViewById(R.id.apply);
        apply.setOnClickListener(v -> applyJob());
    }

    private void findJob(){
        // query the database and find the job by its ID
        DatabaseReference ref = dao.getDatabaseReference();
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
                        if (!newJob.getSelectedApplicant().equalsIgnoreCase("")) {
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
                Log.d("Database Error - findJob(JobAd):", error.getMessage());
            }
        });
    }

    private void applyJob(){
        DatabaseReference ref1 = dao.getDatabaseReference();
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if (newJob != null && newJob.getJobID().matches(jobID)) {
                        List<String> applicants = newJob.getApplicants() == null ? new ArrayList<>() : newJob.getApplicants();
                        if (applicants.contains(grabEmail())) {
                            createToast(R.string.already_applied);
                        }
                        else {
                            DatabaseReference newJobPref = snapshot1.getRef();
                            applicants.add(grabEmail());
                            Map<String, Object> newJobUpdate = new HashMap<>();
                            newJobUpdate.put("applicants", applicants);
                            newJobPref.updateChildren(newJobUpdate);
                            addToAppliedList(jobID);
                            createToast(R.string.applied);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - applyJob(JobAd):", error.getMessage());
            }
        });
    }

    private void addToAppliedList(String jobIDToAdd){
        DAO dao1 = new UserDAOAdapter(new UserDAO());
        DatabaseReference databaseReference = dao1.getDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    if(grabEmail().equals(user.getEmail())){
                        DatabaseReference userRef = dataSnapshot.getRef();
                        Map<String, Object> userUpdate = new HashMap<>();
                        ArrayList<String> ids = user.getAppliedJobs();
                        ids.add(jobIDToAdd);
                        userUpdate.put("appliedJobs", ids);
                        userRef.updateChildren(userUpdate);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }

    /**
     * Returns the email of the user signed in
     * @return
     */
    private String grabEmail(){
        SessionManagerInterface session = SessionManager.getSessionManager(JobAdActivity.this);

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
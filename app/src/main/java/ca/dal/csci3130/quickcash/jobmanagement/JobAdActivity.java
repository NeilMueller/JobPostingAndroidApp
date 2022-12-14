package ca.dal.csci3130.quickcash.jobmanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

    private String jobID;
    private DAO dao;
    private DAO dao1;
    private TextView jobTitle;
    private TextView jobDesc;
    private TextView jobType;
    private TextView jobDuration;
    private TextView jobPayRate;
    private Button apply;
    private TextView ratingTV;
    private String employerID;
    private String userEmail;


    public JobAdActivity() {
        // empty constructor
    }

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_ad);

        dao = new JobDAOAdapter(new JobDAO());
        dao1 = new UserDAOAdapter(new UserDAO());

        jobTitle = findViewById(R.id.jobAdTitle);
        jobDesc = findViewById(R.id.jobAdDescription);
        jobType = findViewById(R.id.jobAdType);
        jobDuration = findViewById(R.id.jobAdDuration);
        jobPayRate = findViewById(R.id.jobAdPayRate);
        ratingTV = findViewById(R.id.ratingTV);
        userEmail = grabEmail();

        // Grab job id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobID = extras.getString("JobID");
        }

        findJob();

        apply = (Button) findViewById(R.id.apply);
        apply.setOnClickListener(v -> applyJob());
    }

    /**
     * Find the requested job and fill UI with its details
     */
    private void findJob() {
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
                        employerID = newJob.getEmployerID();

                        if (!newJob.acceptingApplications()) {
                            String buttonText = "SELECTED";
                            if (!newJob.getSelectedApplicant().equals(userEmail)) {
                                buttonText = "ANOTHER CANDIDATE SELECTED";
                            }
                            apply.setText(buttonText);
                            apply.setEnabled(false);
                        }
                    }

                    displayRating();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - findJob(JobAd):", error.getMessage());
            }
        });
    }

    /**
     * Display the ratings of the user
     */
    protected void displayRating() {
        DatabaseReference ref = dao1.getDatabaseReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && employerID.equals(user.getEmail())) {
                        ratingTV.setText("" + String.format("%.2f", user.getRating()) + "/5");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - displayRating (JobAd):", error.getMessage());
            }
        });
    }

    /**
     * Apply for this job
     */
    private void applyJob() {
        DatabaseReference ref1 = dao.getDatabaseReference();
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Job newJob = snapshot1.getValue(Job.class);
                    if (newJob != null && newJob.getJobID().matches(jobID)) {
                        List<String> applicants = newJob.getApplicants();
                        if (applicants.contains(userEmail)) {
                            createToast(R.string.already_applied);
                        } else {
                            DatabaseReference newJobPref = snapshot1.getRef();
                            applicants.add(userEmail);
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

    /**
     * Add to the user's applied jobs list after the user has applied for the job
     *
     * @param jobIDToAdd
     */
    private void addToAppliedList(String jobIDToAdd) {
        DatabaseReference databaseReference = dao1.getDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    if (user != null && userEmail.equals(user.getEmail())) {
                        DatabaseReference userRef = dataSnapshot.getRef();
                        Map<String, Object> userUpdate = new HashMap<>();
                        List<String> ids = user.getAppliedJobs() == null ? new ArrayList<>() : user.getAppliedJobs();
                        ids.add(jobIDToAdd);
                        userUpdate.put("appliedJobs", ids);
                        userRef.updateChildren(userUpdate);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - addToAppliedList (JobAdActivity)", error.getMessage());
            }
        });
    }

    /**
     * Returns the email of the user signed in
     *
     * @return
     */
    private String grabEmail() {
        SessionManagerInterface session = SessionManager.getSessionManager(JobAdActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn)
            return session.getKeyEmail();

        return null;
    }

    /**
     * method to create Toast message upon error
     *
     * @param messageId
     */
    protected void createToast(int messageId) {
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }
}
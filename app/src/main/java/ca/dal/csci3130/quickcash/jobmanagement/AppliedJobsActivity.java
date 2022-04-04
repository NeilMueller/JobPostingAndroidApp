package ca.dal.csci3130.quickcash.jobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserDAOAdapter;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class AppliedJobsActivity extends AppCompatActivity {

    private final HashMap<String, String> jobItem = new HashMap<>();
    private DAO dao;
    private DAO dao1;
    private String userEmail;
    private List<String> jobIDs;
    private List<Job> jobList;

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);

        jobList = new ArrayList<>();
        jobIDs = new ArrayList<>();
        userEmail = grabEmail();

        dao = new UserDAOAdapter(new UserDAO());
        dao1 = new JobDAOAdapter(new JobDAO());

        Button homePageButton = findViewById(R.id.btn_Employee_Home);

        getJobIDs();
        homePageButton.setOnClickListener(view -> moveToEmployeeHome());
    }

    /**
     * Gets the job ids that the user has applied to
     */
    protected void getJobIDs() {
        DatabaseReference databaseReference = dao.getDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    if (user != null && userEmail.equals(user.getEmail())) {
                        List<String> ids = user.getAppliedJobs();
                        jobIDs.addAll(ids);
                    }
                }

                getJobs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - getJobIDs (AppliedJobs):", error.getMessage());
            }
        });
    }

    /**
     * Gets the jobs that the user has applied to
     */
    private void getJobs() {
        DatabaseReference jobRef = dao1.getDatabaseReference();
        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    if (job != null && jobIDs.contains(job.getJobID())) {
                        if (job.acceptingApplications() || job.getSelectedApplicant().equals(userEmail)) {
                            jobList.add(job);
                        } else {
                            //job accepted someone and it should be removed from our job list
                            removeJob(job.getJobID());
                        }
                    }
                }

                fillList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - getJobs (AppliedJobs):", error.getMessage());
            }
        });
    }


    /**
     * Fills the UI List of applied jobs
     */
    private void fillList() {

        if (jobList.isEmpty()) {
            jobItem.put("No Jobs Applied To", "");
        }

        for (JobInterface job : jobList) {
            jobItem.put(job.getJobTitle(), job.getListedInfo());
        }

        ListView myJobListView = (ListView) findViewById(R.id.lv_applied_jobs);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.my_job_list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.tv_job_title, R.id.tv_job_info});

        myJobListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String itemString = adapter.getItem(i).toString();
            String[] itemStringArr = itemString.split("Job ID:");
            String roughJobID = itemStringArr[1];
            String[] roughJobIDArr = roughJobID.split(",");

            Intent intent = new Intent(getApplicationContext(), EmployeeJobListingActivity.class);
            intent.putExtra("JobID", roughJobIDArr[0]);
            startActivity(intent);

        });

        Iterator<Map.Entry<String, String>> it = jobItem.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry<String, String> pair = it.next();
            resultsMap.put("First Line", pair.getKey());
            resultsMap.put("Second Line", pair.getValue());
            listItems.add(resultsMap);
        }

        myJobListView.setAdapter(adapter);
    }

    /**
     * Remove the jobs that the user has applied to but was not selected
     *
     * @param jobIDtoRemove
     */
    private void removeJob(String jobIDtoRemove) {
        DatabaseReference databaseReference = dao.getDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    if (user != null && userEmail.equals(user.getEmail())) {
                        DatabaseReference userRef = dataSnapshot.getRef();
                        Map<String, Object> userUpdate = new HashMap<>();
                        List<String> ids = user.getAppliedJobs();
                        ids.remove(jobIDtoRemove);
                        userUpdate.put("appliedJobs", ids);
                        userRef.updateChildren(userUpdate);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - removeJob (AppliedJobs):", error.getMessage());
            }
        });
    }

    /**
     * Returns the email of the user signed in
     *
     * @return userEmail
     */
    private String grabEmail() {
        SessionManagerInterface session = SessionManager.getSessionManager(AppliedJobsActivity.this);
        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn) {
            return session.getKeyEmail();
        }
        return null;
    }

    /**
     * Move to Employee Home
     */
    private void moveToEmployeeHome() {
        Intent intent = new Intent(AppliedJobsActivity.this, EmployeeHomeActivity.class);
        startActivity(intent);
    }
}
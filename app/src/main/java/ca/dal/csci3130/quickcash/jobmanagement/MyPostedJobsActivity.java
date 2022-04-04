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
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;

public class MyPostedJobsActivity extends AppCompatActivity {

    private String userEmail;
    private List<Job> jobList;
    private HashMap<String, String> jobItem = new HashMap<>();
    private DAO dao;

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_jobs);

        dao = new JobDAOAdapter(new JobDAO());

        jobList = new ArrayList<>();
        userEmail = grabEmail();

        getJobs();

        Button returnHomeBtn = findViewById(R.id.btnReturnEmployerHome);
        returnHomeBtn.setOnClickListener(view -> moveToEmployerHomePage());
    }


    /**
     * Gets all the jobs from the db that match the user
     */
    protected void getJobs() {
        DatabaseReference jobRef = dao.getDatabaseReference();

        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    if (userEmail.equals(job.getEmployerID())) {
                        jobList.add(job);
                    }
                }

                fillList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - getJobs (MyPostedJobs)", error.getMessage());
            }
        });
    }

    /**
     * Fill the jobs in the UI.
     */
    private void fillList() {

        if (jobList.isEmpty()) {
            jobItem.put("No Posted Jobs", "");
        }

        for (JobInterface job : jobList) {
            jobItem.put(job.getJobTitle(), job.getListedInfo());
        }

        ListView myJobListView = (ListView) findViewById(R.id.myJobsListView);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.my_job_list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.tv_job_title, R.id.tv_job_info});

        myJobListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String itemString = adapter.getItem(i).toString();
            String[] itemStringArr = itemString.split("Job ID:");
            String roughJobID = itemStringArr[1];
            String[] roughJobIDArr = roughJobID.split(",");

            Intent intent = new Intent(getApplicationContext(), EmployerJobListingActivity.class);
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
     * Returns the email of the user signed in
     *
     * @return userEmail
     */
    private String grabEmail() {
        SessionManagerInterface session = SessionManager.getSessionManager(MyPostedJobsActivity.this);
        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn) {
            return session.getKeyEmail();
        }
        return null;
    }

    /**
     * Move to EmployerHome
     */
    private void moveToEmployerHomePage() {
        Intent intent = new Intent(getApplicationContext(), EmployerHomeActivity.class);
        startActivity(intent);
    }
}


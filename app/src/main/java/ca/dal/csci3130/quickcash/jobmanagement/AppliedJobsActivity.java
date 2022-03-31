package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import ca.dal.csci3130.quickcash.common.AbstractDAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class AppliedJobsActivity extends AppCompatActivity {

    private Button homePageButton;
    private String userEmail;
    private List<String> jobIDs;
    private List<Job> jobList;
    HashMap<String, String> jobItem = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);

        jobList = new ArrayList<>();
        jobIDs = new ArrayList<>();
        userEmail = grabEmail();

        homePageButton = findViewById(R.id.btn_Employee_Home);

        getJobIDs();

        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEmployeeHome();
            }
        });
    }

    protected void getJobIDs() {
        AbstractDAO userDAO = new UserDAO();
        DatabaseReference databaseReference = userDAO.getDatabaseReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    if(userEmail.equals(user.getEmail())){
                        ArrayList<String> ids = user.getAppliedJobs();
                        for(String str: ids){
                            jobIDs.add(str);
                        }
                    }
                }

                getJobs();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }

    private void getJobs(){
        JobDAO jobDAO = new JobDAO();
        DatabaseReference jobRef = jobDAO.getDatabaseReference();

        jobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    if(jobIDs.contains(job.getJobID())) {
                        jobList.add(job);
                    }
                }

                fillList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }


    private void fillList(){

        if(jobList.isEmpty()){
            jobItem.put("No Jobs Applied To","");
        }

        for(JobInterface job : jobList) {
            jobItem.put(job.getJobTitle(), job.getListedInfo());
        }

        ListView myJobListView = (ListView) findViewById(R.id.lv_applied_jobs);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems,R.layout.my_job_list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.tv_job_title, R.id.tv_job_info});

        Iterator it = jobItem.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        myJobListView.setAdapter(adapter);
    }

    /**
     * Returns the email of the user signed in
     * @return
     */

    private String grabEmail() {

        SessionManager session = new SessionManager(AppliedJobsActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn){
            return  session.getKeyEmail();
        }
        return null;
    }

    private void moveToEmployeeHome(){
        Intent intent = new Intent(AppliedJobsActivity.this, EmployeeHomeActivity.class);
        startActivity(intent);
    }
}
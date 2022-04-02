package ca.dal.csci3130.quickcash.jobmanagement;

import static android.text.TextUtils.split;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class MyPostedJobsActivity extends AppCompatActivity {


    private String userEmail;
    private List<Job> jobList;
    private Button returnHomebtn;
    HashMap<String, String> jobItem = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_jobs);


        jobList = new ArrayList<>();
        userEmail = grabEmail();

        returnHomebtn = findViewById(R.id.btnReturnEmployerHome);

        getJobs();


        returnHomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {moveToEmployerHomePage();}
        });

    }


    /**
     * Gets all the jobs from the db that match the user
     */
    protected void getJobs() {
        JobDAO jobDAO = new JobDAO();
        DatabaseReference jobRef = jobDAO.getDatabaseReference();

        jobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    if(userEmail.equals(job.getEmployerID())) {
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
            jobItem.put("No Posted Jobs","");
        }

        for(JobInterface job : jobList) {
            jobItem.put(job.getJobTitle(), job.getListedInfo());
        }

        ListView myJobListView = (ListView) findViewById(R.id.myJobsListView);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems,R.layout.my_job_list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.tv_job_title, R.id.tv_job_info});

        myJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemString = adapter.getItem(i).toString();
                String[] itemStringArr = itemString.split("Job ID:");
                String roughJobID = itemStringArr[1];
                String[] roughJobIDArr = roughJobID.split(",");
                String JobID = roughJobIDArr[0];

                Intent intent = new Intent(getApplicationContext(), EmployerJobListingActivity.class);
                intent.putExtra("JobID", JobID);
                startActivity(intent);

            }
        });

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

        SessionManager session = new SessionManager(MyPostedJobsActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn){
            return  session.getKeyEmail();
        }
        return null;
    }

    private void moveToEmployerHomePage() {
        Intent intent = new Intent(getApplicationContext(), EmployerHomeActivity.class);
        startActivity(intent);
    }

}


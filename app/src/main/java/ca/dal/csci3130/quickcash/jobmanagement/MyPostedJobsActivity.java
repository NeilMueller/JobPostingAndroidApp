package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class MyPostedJobsActivity extends AppCompatActivity {

    private List<Job> jobList;
    private String userEmail;
    private Button returnHomebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_jobs);

        //Initialize jobList
        jobList = new ArrayList<>();

        userEmail = grabEmail();

        returnHomebtn = findViewById(R.id.btnReturnEmployerHome);

        returnHomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEmployerHomePage();
            }
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
                    jobList.add(job);
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


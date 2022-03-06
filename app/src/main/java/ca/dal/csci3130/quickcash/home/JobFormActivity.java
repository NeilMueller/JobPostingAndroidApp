package ca.dal.csci3130.quickcash.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAO;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SignupActivity;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class JobFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_form);

        Button postButton = (Button) findViewById(R.id.post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job newJob = getJobData();
                if(newJob != null) {
                    checkAndPushJob(newJob);
                    Intent intent = new Intent(JobFormActivity.this, EmployerHomeActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private Job getJobData() {

        String jobTitle = ((EditText) findViewById(R.id.jobTitle)).getText().toString();
        String jobType = ((EditText) findViewById(R.id.jobType)).getText().toString();
        String jobDescription = ((EditText) findViewById(R.id.description)).getText().toString();
        String durationString = ((EditText) findViewById(R.id.Duration)).getText().toString();
        String payRateString = ((EditText) findViewById(R.id.payRate)).getText().toString();
        int duration = Integer.parseInt(durationString);
        int payRate = Integer.parseInt(payRateString);

        Random random = new Random();
        int num = random.nextInt(999999);

        String numString = String.format("%06d", num);
        durationString = String.format("%02d", duration);

        String jobID = jobType.substring(0, 2) + numString + durationString;


        if(!isEmpty(jobTitle, jobType, jobDescription, duration, payRate)) {
            return new Job(jobTitle, jobType, jobDescription, duration, payRate, jobID);
        }

        return null;
    }

    protected void createToast(int messageId){
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    protected boolean isEmpty(String jobTitle, String jobType, String jobDescription, int duration, int payRate) {
        boolean anyFieldsEmpty = jobTitle.isEmpty() || jobType.isEmpty() || jobDescription.isEmpty()
                || duration <= 0 || payRate <= 0;
        if (anyFieldsEmpty) {
            createToast(R.string.toast_missing_component);
            return true;
        }
        return false;
    }

     //need to to be able to uniquely identify jobs... jobs need a jobID
    private void checkAndPushJob(Job newJob) {
        JobDAO databaseReference = new JobDAO();
        DatabaseReference dataBase = databaseReference.getDatabaseReference();

        dataBase.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean newPosting = true;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Job job = postSnapshot.getValue(Job.class);
                    String jobID = newJob.getJobID();
                    if (job != null && job.getJobID().equals(jobID)) {
                        Toast.makeText(getApplicationContext(), "Job ID already exists", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), EmployerHomeActivity.class);
                        newPosting = false;
                        // intent.putExtra("Email", email);
                        startActivity(intent);
                    }
                }
                if (newPosting) {
                    addJob(newJob);
                    Toast.makeText(getApplicationContext(), "Your job has been posted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }
    protected void addJob(JobInterface job) {
        JobDAO jobDAO = new JobDAO();
        jobDAO.addJob(job);
    }
}
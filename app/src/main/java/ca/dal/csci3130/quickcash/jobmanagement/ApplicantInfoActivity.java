package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;

public class ApplicantInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_info);

        TextView empNameTextView = findViewById(R.id.tv_applicantName);
        TextView empEmailTextView = findViewById(R.id.tv_applicantEmail);
        Button acceptApplicantButton = findViewById(R.id.btn_acceptApplicant);

        String empName = null;
        String empEmail = null;
        String JobID = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            empName = extras.getString("EmpName");
            empEmail = extras.getString("EmpEmail");
            JobID = extras.getString("JobID");
        }
        empNameTextView.setText(empName);
        empEmailTextView.setText(empEmail);

        // when we accept an applicant, the applicant should appear as the jobs "selected applicant"
        // and the job should not accept any more applications
        String finalJobID = JobID.trim();
        String finalEmpEmail = empEmail;
        acceptApplicantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Job");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Job newJob = snapshot1.getValue(Job.class);
                            if (newJob != null && newJob.getJobID().matches(finalJobID)) {
                                DatabaseReference jobRef = snapshot1.getRef();
                                Map<String, Object> jobUpdate = new HashMap<>();
                                jobUpdate.put("selectedApplicant", finalEmpEmail);
                                ArrayList<String> emptyArrayList = new ArrayList<>();
                                emptyArrayList.add("");
                                jobUpdate.put("applicants", emptyArrayList);
                                jobRef.updateChildren(jobUpdate);
                                Toast.makeText(getApplicationContext(), "Candidate accepted",
                                        Toast.LENGTH_LONG).show();
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
}
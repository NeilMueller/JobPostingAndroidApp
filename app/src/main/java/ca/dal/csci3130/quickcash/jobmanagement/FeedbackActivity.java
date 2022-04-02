package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;

public class FeedbackActivity extends AppCompatActivity {

    Button ratingBtn;
    RatingBar ratingBar;

    float myRating = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBtn = findViewById(R.id.ratingBtn);
        ratingBar = findViewById(R.id.ratingBar);
        final EditText employeeEmailET = findViewById(R.id.employeeEmailET);
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myRating = ratingBar.getRating();
            }
        });

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback feedback = new Feedback(employeeEmailET.getText().toString(),myRating);
                feedbackDAO.add(feedback);

                Intent intent = new Intent(getApplicationContext(), EmployerHomeActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Gets all the jobs from the db that match the user
     */
    protected void findUser() {
        UserDAO userDAO = new UserDAO();
        DatabaseReference userRef = userDAO.getDatabaseReference();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    // get jobs and add them to a global list
                    //if(userEmail.equals(job.getEmployerID())) {
                        //jobList.add(job);
                    //}
                }

                //fillList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }
}
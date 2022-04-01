package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

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
}
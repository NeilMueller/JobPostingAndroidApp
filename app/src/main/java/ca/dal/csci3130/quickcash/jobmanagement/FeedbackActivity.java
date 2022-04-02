package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.PreferenceActivity;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;

public class FeedbackActivity extends AppCompatActivity {

    Button ratingBtn;
    TextView userID;
    RatingBar ratingBar;

    double myRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBtn = findViewById(R.id.ratingBtn);
        ratingBar = findViewById(R.id.ratingBar);
        userID = findViewById(R.id.tv_rate_user_ID);
        myRating = 0;


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myRating = ratingBar.getRating();
            }
        });

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateUser();
            }
        });

    }

    /**
     * Gets all the jobs from the db that match the user
     */
    protected void rateUser() {
        UserDAO userDAO = new UserDAO();
        DatabaseReference userRef = userDAO.getDatabaseReference();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);

                    if(user.getEmail().equals(userID.getText())){

                        boolean raterIsEmployer = user.getIsEmployee();

                        //Get new rating
                        double rating = user.getRating();
                        int numberOfRatings = user.getNumberOfRatings();

                        rating = ((rating * numberOfRatings) + myRating) / (numberOfRatings + 1);
                        numberOfRatings++;

                        //push new rating
                        DatabaseReference userRef = dataSnapshot.getRef();
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put("rating", rating);
                        userUpdates.put("numberOfRatings", numberOfRatings);
                        userRef.updateChildren(userUpdates);

                        //display toast
                        createToast(R.string.toast_rating_applied);

                        //move to correct homepage
                        if(raterIsEmployer == true){
                            moveToEmployerHomeActivity();
                        } else {
                            moveToEmployeeHomeActivity();
                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }

    protected void createToast(int messageID){
        Toast.makeText(getApplicationContext(), getString(messageID), Toast.LENGTH_LONG).show();
    }

    private void moveToEmployeeHomeActivity() {
        Intent intent = new Intent(FeedbackActivity.this, EmployeeHomeActivity.class);
        startActivity(intent);
    }

    private void moveToEmployerHomeActivity() {
        Intent intent = new Intent(FeedbackActivity.this, EmployerHomeActivity.class);
        startActivity(intent);
    }
}
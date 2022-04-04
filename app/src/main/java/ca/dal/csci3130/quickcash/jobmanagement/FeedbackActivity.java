package ca.dal.csci3130.quickcash.jobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserDAOAdapter;

public class FeedbackActivity extends AppCompatActivity {

    private TextView userID;
    private float myRating;
    private DAO dao;

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        dao = new UserDAOAdapter(new UserDAO());

        Button ratingBtn = findViewById(R.id.ratingBtn);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        userID = findViewById(R.id.tv_rate_user_ID);
        myRating = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID.setText(extras.getString("userID").trim());
        }

        ratingBar.setOnRatingBarChangeListener((r, v, b) -> myRating = r.getRating());
        ratingBtn.setOnClickListener(view -> rateUser());
    }

    /**
     * Rate the user
     */
    protected void rateUser() {
        DatabaseReference userRef = dao.getDatabaseReference();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.getEmail().equals(userID.getText())) {
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
                        if (raterIsEmployer) {
                            moveToEmployerHomeActivity();
                        } else {
                            moveToEmployeeHomeActivity();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - rateUser (FeedBackActivity)", error.getMessage());
            }
        });
    }

    /**
     * Create a toast
     *
     * @param messageID
     */
    protected void createToast(int messageID) {
        Toast.makeText(this, getString(messageID), Toast.LENGTH_LONG).show();
    }

    /**
     * Move to Employee Home
     */
    private void moveToEmployeeHomeActivity() {
        Intent intent = new Intent(this, EmployeeHomeActivity.class);
        startActivity(intent);
    }

    /**
     * Move to Employer Home
     */
    private void moveToEmployerHomeActivity() {
        Intent intent = new Intent(this, EmployerHomeActivity.class);
        startActivity(intent);
    }
}
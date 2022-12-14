package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class PreferenceActivity extends AppCompatActivity {

    private DAO dao;

    /**
     * Called on activity load
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        dao = new PreferenceDAOAdapter(new PreferenceDAO());

        //Update preferences logic
        Button updatePrefButton = (Button) findViewById(R.id.buttonUpdatePref);

        //Return to home page logic
        Button returnHomeButton = (Button) findViewById(R.id.buttonToEmployeeHome);

        returnHomeButton.setOnClickListener(view -> moveToEmployeeHomeActivity());

        updatePrefButton.setOnClickListener(view -> {
            Preferences pref = getPreferences();
            String id = grabEmail();
            pref.setUserID(id);
            checkAndPush(pref);
            Intent intent = new Intent(PreferenceActivity.this, EmployeeHomeActivity.class);
            startActivity(intent);
        });
    }


    /**
     * Gets all preference data from the UI
     *
     * @return PreferenceObject
     */
    private Preferences getPreferences() {
        //Grab data
        String jobType = ((EditText) findViewById(R.id.editTextJobType)).getText().toString();
        String payRateS = ((EditText) findViewById(R.id.editTextPayRate)).getText().toString();
        String durationS = ((EditText) findViewById(R.id.editTextDuration)).getText().toString();

        double payRate;
        double duration;

        if (isEmpty(jobType, payRateS, durationS)) {
            //Default pay rate is 0
            if (payRateS.isEmpty()) {
                payRate = 0;
            } else {
                payRate = Double.parseDouble(payRateS);
            }
            //Default duration is 100
            if (durationS.isEmpty()) {
                duration = 100;
            } else {
                duration = Double.parseDouble(durationS);
            }
        } else {
            payRate = Double.parseDouble(payRateS);
            duration = Double.parseDouble(durationS);
        }

        return new Preferences(jobType, payRate, duration);
    }

    /**
     * method to check if any of the preference fields are empty
     * if any empty return true
     *
     * @param jobType
     * @param payRate
     * @param duration
     * @return
     */
    private boolean isEmpty(String jobType, String payRate, String duration) {
        boolean anyFieldsEmpty = jobType.isEmpty() || payRate.isEmpty() || duration.isEmpty();

        if (anyFieldsEmpty) {
            createToast(R.string.toast_missing_component);
            return true;
        }

        return false;
    }

    /**
     * Update/Add preferences of the user.
     *
     * @param newPreferences
     */
    private void checkAndPush(Preferences newPreferences) {
        DatabaseReference dataBase = dao.getDatabaseReference();
        dataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean newPreference = true;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Preferences preferences = postSnapshot.getValue(Preferences.class);
                    String userID = newPreferences.getUserID();
                    if (preferences != null && preferences.getUserID().equals(userID)) {
                        DatabaseReference prefRef = postSnapshot.getRef();
                        Map<String, Object> prefUpdates = new HashMap<>();
                        prefUpdates.put("jobType", newPreferences.getJobType());
                        prefUpdates.put("duration", newPreferences.getDuration());
                        prefUpdates.put("payRate", newPreferences.getPayRate());
                        prefRef.updateChildren(prefUpdates);
                        Toast.makeText(getApplicationContext(), "Preferences Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), EmployeeHomeActivity.class);
                        newPreference = false;
                        startActivity(intent);
                    }
                }

                if (newPreference) {
                    addPreferences(newPreferences);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - checkAndPush(Preferences):", error.getMessage());
            }
        });
    }

    /**
     * Push preferences to db
     *
     * @param preferences
     */
    protected void addPreferences(PreferenceInterface preferences) {
        dao.add(preferences);
    }

    /**
     * Returns the email of the user signed in
     *
     * @return
     */
    private String grabEmail() {
        SessionManagerInterface session = SessionManager.getSessionManager(PreferenceActivity.this);
        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn) {
            return session.getKeyEmail();
        }
        return null;
    }

    /**
     * Create a toast
     *
     * @param messageId
     */
    protected void createToast(int messageId) {
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    /**
     * Move to Employee Home
     */
    private void moveToEmployeeHomeActivity() {
        Intent intent = new Intent(PreferenceActivity.this, EmployeeHomeActivity.class);
        startActivity(intent);
    }
}
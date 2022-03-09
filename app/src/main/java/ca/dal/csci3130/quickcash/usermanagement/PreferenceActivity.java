package ca.dal.csci3130.quickcash.usermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        //Update preferences logic
        Button updatePrefButton = (Button) findViewById(R.id.buttonUpdatePref);

        //Return to home page logic
        Button returnHomeButton = (Button) findViewById(R.id.buttonToEmployeeHome);

        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEmployeeHomeActivity();
            }
        });

        updatePrefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences pref = getPreferences();
                String id = grabEmail();
                pref.setUserID(id);
                if (pref != null){
                    //Update preferences
                    checkAndPush(pref);
                    Intent intent = new Intent(PreferenceActivity.this, EmployeeHomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * Gets all preference data from the UI
     * @return PreferenceObject
     */

    private Preferences getPreferences(){

        //Grab data
        String jobType = ((EditText) findViewById(R.id.editTextJobType)).getText().toString();
        String payRateS = ((EditText) findViewById(R.id.editTextPayRate)).getText().toString();
        String distanceS = ((EditText) findViewById(R.id.editTextDistance)).getText().toString();
        String durationS = ((EditText) findViewById(R.id.editTextDuration)).getText().toString();

        if(isEmpty(jobType, payRateS, distanceS, durationS)){
            return null;
        }

        double payRate = Double.valueOf(payRateS);
        int distance = Integer.valueOf(distanceS);
        int duration = Integer.valueOf(durationS);

        return new Preferences(jobType, payRate, distance, duration);
    }

    /**
     * method to check if any of the preference fields are empty
     * if any empty return true
     *
     * @param jobType
     * @param payRate
     * @param distance
     * @param duration
     * @return
     */

    private boolean isEmpty(String jobType, String payRate, String distance, String duration){

        boolean anyFieldsEmpty = jobType.isEmpty() || payRate.isEmpty() || distance.isEmpty() || duration.isEmpty();

        if (anyFieldsEmpty) {
            createToast(R.string.toast_missing_component);
            return true;
        }

        return false;
    }

    private void checkAndPush(Preferences newPreferences){

        PreferenceDAO databaseReference = new PreferenceDAO();
        DatabaseReference dataBase = databaseReference.getDatabaseReference();

        dataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean newPreference = true;
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Preferences preferences = postSnapshot.getValue(Preferences.class);
                    String userID = newPreferences.getUserID();
                    if (preferences != null && preferences.getUserID().equals(userID)) {
                        Toast.makeText(getApplicationContext(), "Preferences already set", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), EmployeeHomeActivity.class);
                        newPreference = false;
                        startActivity(intent);
                    }
                }

                if (newPreference) addPreferences(newPreferences);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String errorRead = error.getMessage();
            }
        });
    }

    protected void addPreferences(PreferenceInterface preferences){
        PreferenceDAO preferenceDAO = new PreferenceDAO();
        preferenceDAO.addPreference(preferences);
    }

    private String grabEmail() {

        SessionManager session = new SessionManager(PreferenceActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn){
            return  session.getKeyEmail();
        }
        return null;
    }



    protected void createToast(int messageId){
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    private void moveToEmployeeHomeActivity() {
        Intent intent = new Intent(PreferenceActivity.this, EmployeeHomeActivity.class);
        startActivity(intent);
    }
}
package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

public class PreferenceDAO {
    private final DatabaseReference databaseReference;

    public PreferenceDAO(){
        // FIREBASE_URL needs to be updated
        FirebaseDatabase db = FirebaseDatabase.
                getInstance(Constants.FIREBASE_URL);
        databaseReference = db.getReference(Preferences.class.getSimpleName());
    }

    public DatabaseReference getPreferenceDatabaseReference() { return databaseReference;}

    public Task<Void> addPreference(PreferenceInterface preference) {
        return databaseReference.push().setValue(preference);
    }
}

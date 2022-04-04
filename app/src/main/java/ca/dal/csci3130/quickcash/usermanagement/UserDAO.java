package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

/**
 * Used by UserDAOAdapter to provide UserDAO's functionalities
 */
public class UserDAO {
    private final DatabaseReference databaseReference;

    public UserDAO() {
        FirebaseDatabase db = FirebaseDatabase.
                getInstance(Constants.FIREBASE_URL);
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public Task<Void> addUser(UserInterface user) {
        return databaseReference.push().setValue(user);
    }

}

package ca.dal.csci3130.quickcash.common;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public abstract class AbstractDAO {
    public DatabaseReference getDatabaseReference() {
        return null;
    }

    public Task<Void> add(UserInterface user) {
        return null;
    }

}

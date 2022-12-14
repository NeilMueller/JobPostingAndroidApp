package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

/**
 * JobDAO used by JobDAOAdapter
 */
public class JobDAO {
    private final DatabaseReference databaseReference;

    public JobDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        this.databaseReference = db.getReference(Job.class.getSimpleName());
    }

    public DatabaseReference getJobDatabaseReference() {
        return databaseReference;
    }

    public Task<Void> addJob(JobInterface job) {
        return databaseReference.push().setValue(job);
    }
}

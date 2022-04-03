package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackDAO {

    private DatabaseReference databaseReference;

    public FeedbackDAO(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Feedback.class.getSimpleName());
    }

    public Task<Void> add (Feedback feedback){
        return databaseReference.push().setValue(feedback);
    }
}

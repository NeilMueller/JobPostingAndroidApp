package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

/**
 * Provides the functionalities of a JobDAO to an abstract DAO
 */
public class JobDAOAdapter extends DAO {

    private final JobDAO jobDAO;

    public JobDAOAdapter(JobDAO jobDAO){
        this.jobDAO = jobDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return jobDAO.getJobDatabaseReference();
    }

    @Override
    public Task<Void> add(Object obj){
        if(obj instanceof JobInterface){
            return jobDAO.addJob((Job) obj);
        }
        return null;
    }
}

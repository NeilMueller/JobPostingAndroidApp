package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

public class UserDAOAdapter extends DAO {

    private UserDAO userDAO;

    public UserDAOAdapter(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return userDAO.getDatabaseReference();
    }

    @Override
    public Task<Void> add(Object obj){
        if(obj instanceof UserInterface){
            return userDAO.addUser((User) obj);
        }
        return null;
    }
}
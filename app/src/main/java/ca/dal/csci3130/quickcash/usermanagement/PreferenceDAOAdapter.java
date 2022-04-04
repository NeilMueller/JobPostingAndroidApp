package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

/**
 * Provides the functionalities of a PreferenceDAO to an abstract DAO
 */
public class PreferenceDAOAdapter extends DAO {

    private final PreferenceDAO preferenceDAO;

    public PreferenceDAOAdapter(PreferenceDAO preferenceDAO) {
        this.preferenceDAO = preferenceDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return preferenceDAO.getPreferenceDatabaseReference();
    }

    @Override
    public Task<Void> add(Object obj) {
        if (obj instanceof PreferenceInterface) {
            return preferenceDAO.addPreference((Preferences) obj);
        }
        return null;
    }
}

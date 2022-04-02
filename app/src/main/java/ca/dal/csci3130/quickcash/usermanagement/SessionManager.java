package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager implements SessionManagerInterface {

    static final String SHARE_PREF_NAME = "session";
    private static SessionManager sessionManager;
    SharedPreferences.Editor editor;
    private final SharedPreferences pref;

    private SessionManager(Context context) {
        pref = context.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static SessionManagerInterface getSessionManager(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    @Override
    public void createLoginSession(String email, String password, String name, boolean isEmployee) {
        editor.putString("email", email).commit();
        editor.putString("password", password).commit();
        editor.putString("name", name).commit();
        editor.putBoolean("isEmployee", isEmployee).commit();
    }

    @Override
    public void checkLogin() {
        // for interface implementation only. Not in use.
    }

    @Override
    public void logoutUser() {
        editor.clear().commit();
    }

    @Override
    public boolean isLoggedIn() {
        return !getKeyEmail().equals("") && !getKeyName().equals("");
    }

    @Override
    public String getKeyName() {
        return pref.getString("name", "");
    }

    @Override
    public String getKeyEmail() {
        return pref.getString("email", "");
    }

    @Override
    public boolean getIsEmployee() {
        return pref.getBoolean("isEmployee", false);
    }
}

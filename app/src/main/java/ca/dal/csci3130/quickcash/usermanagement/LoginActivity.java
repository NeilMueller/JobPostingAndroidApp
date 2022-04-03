package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

public class LoginActivity extends AppCompatActivity {

    private DAO dao;

    @Override
    public void onBackPressed() {
        // do nothing when pressed back
    }

    /**
     * Checks if user is logged in
     */

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dao = new UserDAOAdapter(new UserDAO());

        TextView registerRedirect = (TextView) findViewById(R.id.newUserRedirect);
        registerRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        Button loginButtonCheck = (Button) findViewById(R.id.loginButtonCheckInfo);

        loginButtonCheck.setOnClickListener(view -> {
            String[] loginDetails = getLoginData();
            if (loginDetails.length != 0)
                checkCredentialsAndLogin(loginDetails);
        });
    }

    /**
     * Gets loginEmail and loginPassword attempted by the UI. Validates input.
     * If the data is valid, returns a String[] array containing [0]loginEmail, [1]loginPassword
     * else it will return null
     *
     * @return String[] loginDetails
     */
    private String[] getLoginData() {
        String[] loginDetails;
        String loginEmail = ((EditText) findViewById(R.id.etEmailId)).getText().toString();
        String loginPassword = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        if (!isEmpty(loginEmail, loginPassword) && isValidEmail(loginEmail)) {
            loginDetails = new String[]{loginEmail, loginPassword};
            return loginDetails;
        }
        return new String[]{};
    }

    /**
     * checks the credentials and redirects to the respective home page
     * @param loginDetails
     */
    protected void checkCredentialsAndLogin(String[] loginDetails) {
        DatabaseReference databaseReference = dao.getDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean loggedIn = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserInterface user = dataSnapshot.getValue(User.class);
                    if(checkCredentials(loginDetails, user)){
                        // create session and redirect to home page
                        loggedIn = true;
                        createToast(R.string.toast_login_successful);
                        login(user);
                        break;
                    }
                }
                // If no emailID or password match throw an error to the user
                if (!loggedIn)
                    createToast(R.string.toast_invalid_email_and_or_password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error - checkCredentialsAndLogin(Login):", error.getMessage());
            }
        });
    }

    /**
     * Check if any login field is empty
     *
     * @param loginEmail
     * @param loginPassword
     * @return true or false
     */
    protected boolean isEmpty(String loginEmail, String loginPassword) {
        boolean anyFieldsEmpty = loginEmail.isEmpty() || loginPassword.isEmpty();
        if (anyFieldsEmpty) {
            createToast(R.string.toast_missing_component);
            return true;
        }
        return false;
    }

    /**
     * method to validate if the email entered is valid
     *
     * @param email
     * @return true or false
     */
    protected boolean isValidEmail(String email) {
        boolean isEmailValid = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
        if (!isEmailValid) {
            createToast(R.string.toast_invalid_email);
            return false;
        }
        return true;
    }

    /**
     * method to create Toast message upon error
     *
     * @param messageId
     */
    protected void createToast(int messageId) {
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    /**
     * Returns true or false based on whether credentials are valid or not
     * @param loginDetails
     * @param user
     * @return true or false
     */
    protected boolean checkCredentials(String[] loginDetails, UserInterface user){
        if (user != null && user.getEmail().equalsIgnoreCase(loginDetails[0])) {
            String decryptedPassword = decryptUserPassword(user.getPassword()); //test
            if(decryptedPassword.equals(loginDetails[1]))
                return true;
        }

        return false;
    }

    /**
     * Decrypts user password and returns that string.
     *
     * @param password
     * @return decryptedPassword
     */
    //Decrypt
    protected String decryptUserPassword(String password){

        StringBuilder result = new StringBuilder("");
        int key = 3; // can make key variable in the future
        for (int x = 0; x < password.length(); x++) {
            char letter = password.charAt(x);
            if (Character.isLowerCase(letter)) {
                char newLetter = (char) (letter - key);
                if (letter < ('a' + key)) {
                    result.append((char) (letter + (26 - key)));
                } else {
                    result.append(newLetter);
                }
            } else if (Character.isUpperCase(letter)) {
                char newLetter = (char) (letter - key);
                if (letter < ('A' + key)) {
                    result.append((char) (letter + (26 - key)));
                } else {
                    result.append(newLetter);
                }
            } else {
                result.append(letter);
            }
        }

        return String.valueOf(result);
    }


    /**
     * Creates a session for the user and redirects them to their respective home page
     *
     * @param
     */
    public void login(UserInterface user) {
        SessionManagerInterface session = SessionManager.getSessionManager(LoginActivity.this);
        String fullName = user.getFirstName() + " " + user.getLastName();

        session.createLoginSession(user.getEmail(), user.getPassword(), fullName, user.getIsEmployee());

        boolean isEmployee = user.getIsEmployee();

        if (isEmployee) {
            moveToEmployeePage();
        } else {
            moveToEmployerPage();
        }
    }

    /**
     * Shift to the Employee home page
     */
    private void moveToEmployeePage() {

        Intent intentEmployee = new Intent(LoginActivity.this, EmployeeHomeActivity.class);
        intentEmployee.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentEmployee);
    }

    /**
     * Shift to the Employer home page
     *
     *
     */
    private void moveToEmployerPage() {

        Intent intentEmployer = new Intent(LoginActivity.this, EmployerHomeActivity.class);
        intentEmployer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intentEmployer);
    }



    /**
     * Checks if session already exists and moves user to home page session exists
     */
    private void checkSession() {
        SessionManagerInterface session = SessionManager.getSessionManager(LoginActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if (isLoggedIn) {
            boolean isEmployee = session.getIsEmployee();
            if (isEmployee) {
                moveToEmployeePage();
            } else {
                moveToEmployerPage();
            }
        }
    }


}
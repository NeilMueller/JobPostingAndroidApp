package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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

import java.util.regex.Pattern;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView registerRedirect;

    @Override
    public void onBackPressed () {
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

        registerRedirect = (TextView) findViewById(R.id.newUserRedirect);
        registerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        Button loginButtonCheck = (Button) findViewById(R.id.loginButtonCheckInfo);

        loginButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] loginDetails = getLoginData();
            }
        });

//        String intentEmail = getIntent().getStringExtra("Email");
//        EditText email = (EditText) findViewById(R.id.etEmailId);
//        if(!intentEmail.isEmpty()){
//            email.append(intentEmail);
//        }


    }

    /**
     * Gets loginEmail and loginPassword attempted by the UI. Validates input.
     * If the data is valid, returns a String[] array containing [0]loginEmail, [1]loinPassword
     * else it will return null
     *
     * @return String[] loginDetails
     */
    private String[] getLoginData() {
        String[] loginDetails = new String[2];
        String loginEmail = ((EditText) findViewById(R.id.etEmailId)).getText().toString();
        String loginPassword = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        if (!isEmpty(loginEmail, loginPassword)) {
            if(isValidEmail(loginEmail)) {
                return loginDetails;
            }
        }
        return null;
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
     * @param messageId
     */
    protected void createToast(int messageId){
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param
     */
    public void login(String email, String password, String name){
        SessionManager session = new SessionManager(LoginActivity.this);

        session.createLoginSession(email, password, name);

        moveToHomePage("employee");    //CHANGE TO USER

    }

    /**
     *
     *
     */
    private void moveToHomePage(String userType){
        if(userType.equals("employee")){
            Intent intentEmployee = new Intent(LoginActivity.this, EmployeeHomeActivity.class);
            startActivity(intentEmployee);
        } else if (userType.equals("employer")){
            Intent intentEmployer = new Intent(LoginActivity.this, EmployerHomeActivity.class);
            startActivity(intentEmployer);
        }

    }

    /**
     * Checks if session already exists and moves user to home page session exists
     *
     */

    private void checkSession(){
        SessionManager session = new SessionManager(LoginActivity.this);

        boolean isLoggedIn = session.isLoggedIn();

        if(isLoggedIn){
            moveToHomePage("employee");        //CHANGE TO USER
        }
    }
}
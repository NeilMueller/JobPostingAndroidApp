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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.AbstractDAO;

public class LoginActivity extends AppCompatActivity {

    private TextView registerRedirect;

    @Override
    public void onBackPressed () {
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
                if(loginDetails!=null){
                    AbstractDAO userDAO = new UserDAO();
                    DatabaseReference databaseReference = userDAO.getDatabaseReference();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    UserInterface user = dataSnapshot.getValue(User.class);
                                    if(user.getEmail().toLowerCase().equals(loginDetails[0].
                                            toLowerCase())){
                                        //pull and decrypt password tbd
                                        if(user.getPassword().equals(loginDetails[1])){
                                            // create session
                                            // move to the home page based on the type
                                            createToast(R.string.toast_login_successful);

                                        }
                                    }
                                }
                                // If no emailID or password match throw an error to the user
                                createToast(R.string.toast_invalid_email_and_or_password);
                            }
                            else{

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //TBD error correction
                        }
                    });
                    //user
                }
            }
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
        if (!isEmpty(loginEmail, loginPassword)) {
            if(isValidEmail(loginEmail)) {
                loginDetails = new String[]{loginEmail, loginPassword};
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
}
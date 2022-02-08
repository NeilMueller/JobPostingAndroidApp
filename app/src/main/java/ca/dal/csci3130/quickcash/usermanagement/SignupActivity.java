package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import ca.dal.csci3130.quickcash.R;

public class SignupActivity extends AppCompatActivity {

    UserDAO userDAO = new UserDAO();
    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // logic for signup
        Button signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUser = getUserData();      //DON'T CREATE NEW USER HERE ADD NEW METHOD
                if(newUser != null) {
                    userDAO.add(newUser); // push to DB if data is valid
                    // ET7: redirect to successful registration page
                }
            }
        });
    }

    public void addUser(UserInterface user){
        userDAO.add(user);
    }
    /**
     * Gets all the user Data from the UI. Validates it.
     * If the data is valid, returns a User Object created from that data
     * else it will return null
     * @return UserObject or null
     */
    private User getUserData(){

        // get all data
        String firstName = ((EditText) findViewById(R.id.etFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.etLastName)).getText().toString();
        String email = ((EditText) findViewById(R.id.etEmailIdSignUp)).getText().toString();

        String phoneStr = ((EditText) findViewById(R.id.etPhoneNumber)).getText().toString();
        int phone = phoneStr.isEmpty() ? 0 : Integer.parseInt(phoneStr);

        String password = ((EditText) findViewById(R.id.etPasswordSignUp)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.etConfirmPasswordSignUp)).getText().toString();

        RadioGroup userTypeRadioGroup = (RadioGroup) findViewById(R.id.userTypeRadioGroup);
        boolean isEmployee = ((RadioButton) findViewById(userTypeRadioGroup.getCheckedRadioButtonId()))
                .getText().toString().equals("Employee");

        // validate all data
        boolean Empty = isEmpty(firstName, lastName, email, phone, password, confirmPassword);
        boolean validEmail = isValidEmail(email);
        boolean passwordLen =  passwordLength(password);
        boolean passwordCon = passwordConfirmation(confirmPassword, password);
        boolean phoneLen = phoneLength(phone);

        if(Empty || validEmail || passwordLen || passwordCon || phoneLen)
            return null;

        accountExists(email);




        // encrypt user password
        password = encryptUserPassword(password);

        return new User(firstName, lastName, email, phone, password, isEmployee);
    }

    /**
     * Validates all data and throws appropriate errors
     * Return true if all data is valid, else it returns false
     * @param firstName
     * @param lastName
     * @param email
     * @param phone
     * @param password
     * @param confirmPassword
     * @return true or false
     */

    // validation goes here
    // check if something is entered in all the fields
    protected boolean isEmpty (String firstName, String lastName, String email, int phone, String password, String confirmPassword) {
        boolean anyFieldsEmpty = firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || phone == 0 || password.isEmpty() || confirmPassword.isEmpty();
        if (anyFieldsEmpty) {
            Toast.makeText(getApplicationContext(),
                    R.string.toast_missing_component, Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

    // check if the email pattern is correct
    protected boolean isValidEmail(String email) {
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!isEmailValid) {
            Toast.makeText(getApplicationContext(),
                    R.string.toast_invalid_email, Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }


    // check if the password is at least 8 characters
    protected boolean passwordLength(String password) {
        final String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        boolean passwordPatternMatches = Pattern.compile(passwordPattern).matcher(password).matches();
        if (!passwordPatternMatches) {
            Toast.makeText(getApplicationContext(),
                    R.string.toast_invalid_password, Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

    // check if the password and the confirmed password are the same
    protected boolean passwordConfirmation(String confirmPassword, String password) {
        boolean passwordMatches = password.equals(confirmPassword);
        if (!passwordMatches) {
            Toast.makeText(getApplicationContext(),
                    R.string.toast_password_mismatch, Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }




     // check if the phone number is 10 digits
    protected boolean phoneLength(int phone) {
        final String phonePattern = "^[0-9]{10}$";
        boolean isPhoneNumberValid = Patterns.PHONE.matcher("" + phone).matches();
            if (!isPhoneNumberValid) {
                Toast.makeText(getApplicationContext(),
                    R.string.toast_invalid_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }


        private void accountExists(String email){
            UserDAO databaseReference = new UserDAO();
            DatabaseReference dataBase= databaseReference.getDatabaseReference();

            dataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postSnapshot: snapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        if (user.getEmail().equals(email)) {
                            Toast.makeText(getApplicationContext(), "email already exists please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("Email", email);
                            startActivity(intent);
                        }
                    }

                }
                @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    final String errorRead= error.getMessage();
                }
            });


        }

    /**
     * Encrypts user password and returns and the encrypted password.
     * @param password
     * @return encryptedPassword
     */
    private String encryptUserPassword(String password){
        // ET5: encrypt user password here
        return password;
    }
}
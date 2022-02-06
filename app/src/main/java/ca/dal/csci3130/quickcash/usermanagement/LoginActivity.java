package ca.dal.csci3130.quickcash.usermanagement;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String intentEmail = getIntent().getStringExtra("Email");
        EditText email = (EditText) findViewById(R.id.etEmailId);
        if(!intentEmail.isEmpty()){
            email.append(intentEmail);
        }




    }
}
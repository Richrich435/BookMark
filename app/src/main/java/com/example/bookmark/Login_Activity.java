package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    Button SignInButton;
    Button RegisterAccount;
    ProgressDialog LoadingBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        inputEmail = findViewById(R.id.Email);
        inputPassword = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        LoadingBar = new ProgressDialog(this);
        ButtonFunctionality();

    }
    private void ButtonFunctionality()
    {
        SignInButton = findViewById(R.id.BTNlogin);
        SignInButton.setOnClickListener(v -> {
            login();
        });

        RegisterAccount = (Button) findViewById(R.id.BTNregister);
        RegisterAccount.setOnClickListener(v -> {
            Intent registerUser = new Intent(Login_Activity.this,Register_Activity.class);
            startActivity(registerUser);
        });
    }

    private void login()
    {
        String txt_email = inputEmail.getText().toString();
        String txt_password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password)) {
            Toast.makeText(Login_Activity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(txt_email)) {
            Toast.makeText(Login_Activity.this, "This email entered is not valid.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoadingBar.setTitle("Logging in");
            LoadingBar.setMessage("Please wait");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    LoadingBar.dismiss();
                    Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login_Activity.this, AddLocationReview_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    LoadingBar.dismiss();
                    Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static boolean isValidEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
package com.example.bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputNumber;
    TextView AlreadyHaveAccount;
    FirebaseAuth auth;
    FirebaseUser User;
    DatabaseReference dbReference;
    ProgressDialog LoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        inputName = findViewById(R.id.name);
        inputEmail = findViewById(R.id.Email);
        inputPassword = findViewById(R.id.password);
        inputNumber = findViewById(R.id.PhoneNumber);
        AlreadyHaveAccount = findViewById(R.id.AlreadyHaveAccount);

        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");
        LoadingBar = new ProgressDialog(this);
        buttonFunctionality();

    }

    private void buttonFunctionality()
    {
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            register();
        });
        AlreadyHaveAccount.setOnClickListener(v -> {
            System.out.println("WHY DO YOU WORK BUT THE OTHER ONE DOESNT");
            Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
            startActivity(intent);
        });
    }

    private void register() {
        String txt_username = inputName.getText().toString();
        String txt_email = inputEmail.getText().toString();
        String txt_password = inputPassword.getText().toString();
        String txt_phoneNumber = inputNumber.getText().toString();
        isValidEmail(txt_email);
        isValidPhoneNumber(txt_phoneNumber);
        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) ||
                TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_phoneNumber)) {
            Toast.makeText(Register_Activity.this, "All fields are required.",
                    Toast.LENGTH_SHORT).show();
        } else if (txt_password.length() < 6) {
            Toast.makeText(Register_Activity.this, "The password entered is not valid.",
                    Toast.LENGTH_SHORT).show();
        } else if (!isValidPhoneNumber(txt_phoneNumber)) {
            Toast.makeText(Register_Activity.this, "This phone number entered is not valid.",
                    Toast.LENGTH_SHORT).show();

        } else if (!isValidEmail(txt_email)) {
            Toast.makeText(Register_Activity.this, "This email entered is not valid.",
                    Toast.LENGTH_SHORT).show();
        } else if (txt_username.length() < 3) {
            Toast.makeText(Register_Activity.this, "This username is too short.",
                    Toast.LENGTH_SHORT).show();
        } else if (txt_username.length() > 10) {
            Toast.makeText(Register_Activity.this, "This username is too long.",
                    Toast.LENGTH_SHORT).show();
        } else {
            LoadingBar.setTitle("Registering");
            LoadingBar.setMessage("Please wait");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    LoadingBar.dismiss();
                    HashMap<String, Object> UserData = new HashMap<>();
                    UserData.put("username",txt_username);
                    UserData.put("phone number", txt_phoneNumber);

                    dbReference.child(User.getUid()).updateChildren(UserData).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(Register_Activity.this,"Set up profile",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register_Activity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(Register_Activity.this, AddLocationReview_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Register_Activity.this, "You can't register without this email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }
    static boolean isValidEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    static boolean isValidPhoneNumber(String s){
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        //1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile(patterns);
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }
}

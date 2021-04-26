package com.example.bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddLocationReview_Activity extends AppCompatActivity {

    private EditText LocationName, LocationComment;
    FirebaseAuth auth;
    FirebaseUser User;
    DatabaseReference UserReference;
    Button AddMoreLocationReviews;
    Button CreateProfile;
    int numberOfStars;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_review);
        RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar); // initiate a rating bar
        numberOfStars = simpleRatingBar.getNumStars(); // get total number of stars of rating bar
        LocationName = findViewById(R.id.LocationName);
        LocationComment = findViewById(R.id.Comment);

        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        UserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        AddMoreLocationReviews = (Button) findViewById(R.id.AddMoreLocations);
        CreateProfile = (Button) findViewById(R.id.Continue);
        ButtonFunctionality();
        StoreLocationInfoIntoFireBase();
    }
    private void ButtonFunctionality()
    {
        AddMoreLocationReviews.setOnClickListener(v -> {
            StoreLocationInfoIntoFireBase();
        });
        CreateProfile.setOnClickListener(v -> {
            StoreLastLocationInfo();
        });
    }

    private void StoreLocationInfoIntoFireBase()
    {
        UserId = User.getUid();
        String txt_LocationName = LocationName.getText().toString();
        String txt_LocationComment = LocationComment.getText().toString();
        if (TextUtils.isEmpty(txt_LocationName) || TextUtils.isEmpty(txt_LocationComment)) {
            Toast.makeText(AddLocationReview_Activity.this, "All fields are required.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> LocationInformation = new HashMap<>();
            LocationInformation.put("LocationName", txt_LocationName);
            LocationInformation.put("LocationComment", txt_LocationComment);
            LocationInformation.put("Star Rating", numberOfStars);
            UserReference.child(UserId).child("Location").push().setValue(LocationInformation).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(AddLocationReview_Activity.this,"Location added",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddLocationReview_Activity.this, AddLocationReview_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddLocationReview_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void StoreLastLocationInfo()
    {
        UserId = User.getUid();
        String txt_LocationName = LocationName.getText().toString();
        String txt_LocationComment = LocationComment.getText().toString();
        if (TextUtils.isEmpty(txt_LocationName) || TextUtils.isEmpty(txt_LocationComment)) {
            Toast.makeText(AddLocationReview_Activity.this, "All fields are required.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> LocationInformation = new HashMap<>();
            LocationInformation.put("LocationName", txt_LocationName);
            LocationInformation.put("LocationComment", txt_LocationComment);
            LocationInformation.put("Star Rating", numberOfStars);
            UserReference.child(UserId).child("Location").push().setValue(LocationInformation).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(AddLocationReview_Activity.this,"Location added",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddLocationReview_Activity.this, AddLocationReview_Activity.class);    //THIS NEEDS TO BE CHANGED TO MAP VIEW
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddLocationReview_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




}
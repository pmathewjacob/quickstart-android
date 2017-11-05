package com.google.firebase.quickstart.database;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.models.RegisterUser;

public class RegisterUserDetailActivity extends BaseActivity{

    private static final String TAG = "RegisterUserDetailAct";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private TextView mName;
    private TextView mPhoneNumber;
    private TextView mAge;
    private TextView mParish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_detail);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("register")
                .child(getUid())
                .child(mPostKey);

        // Initialize Views
        mName = findViewById(R.id.register_user_detail_fName);
        mPhoneNumber = findViewById(R.id.register_user_detail_phoneNumber);
        mParish = findViewById(R.id.register_user_detail_Parish);
        mAge = findViewById(R.id.register_user_detail_Age);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get RegisterUser object and use the values to update the UI
                RegisterUser registerUser = dataSnapshot.getValue(RegisterUser.class);
                // [START_EXCLUDE]
                if (registerUser != null) {
                    mName.setText(registerUser.fullName);
                    mPhoneNumber.setText(registerUser.phoneNumber);
                    mAge.setText(registerUser.age);
                    mParish.setText(registerUser.parish);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(RegisterUserDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }
}

package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.quickstart.database.models.Post;
import com.google.firebase.quickstart.database.models.RegisterUser;
import com.google.firebase.quickstart.database.models.User;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mphoneNumberField;
    private EditText mFullNameField;
    private EditText mAgeField;
    private EditText mParishField;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mphoneNumberField = findViewById(R.id.field_phoneNumber);
        mFullNameField = findViewById(R.id.field_fName);
        mAgeField = findViewById(R.id.field_Age);
        mParishField = findViewById(R.id.field_Parish);
        mSignUpButton = findViewById(R.id.button_sign_up);

        // Click listeners
        mSignUpButton.setOnClickListener(this);
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String phoneNumber = mphoneNumberField.getText().toString();
        final String fullName = mFullNameField.getText().toString();
        final String parish = mParishField.getText().toString();
        final String age = mAgeField.getText().toString();

        if(mAuth.getCurrentUser() != null)
            registerUser(mAuth.getCurrentUser().getUid(), phoneNumber, fullName, parish, age);
        hideProgressDialog();
        // Go to MainActivity
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mphoneNumberField.getText().toString())) {
            mphoneNumberField.setError("Required");
            result = false;
        } else {
            mphoneNumberField.setError(null);
        }

        if (TextUtils.isEmpty(mFullNameField.getText().toString())) {
            mFullNameField.setError("Required");
            result = false;
        } else {
            mFullNameField.setError(null);
        }

        if (TextUtils.isEmpty(mParishField.getText().toString())) {
            mParishField.setError("Required");
            result = false;
        } else {
            mParishField.setError(null);
        }

        if (TextUtils.isEmpty(mAgeField.getText().toString())) {
            mAgeField.setError("Required");
            result = false;
        } else {
            mAgeField.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void registerUser(String userId, String phoneNumber, String fullName, String parish, String age) {
        RegisterUser registerUser = new RegisterUser(phoneNumber, fullName, parish, age);

        String key = mDatabase.child("register").push().getKey();
        Map<String, Object> postValues = registerUser.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/register/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {
        int i = v.getId();
    if (i == R.id.button_sign_up) {
            signUp();
        }
    }
}

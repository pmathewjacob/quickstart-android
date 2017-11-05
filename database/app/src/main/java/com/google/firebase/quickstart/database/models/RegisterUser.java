package com.google.firebase.quickstart.database.models;

/*
 * Created by mathewjacob on 29/10/17.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class RegisterUser {

    public String phoneNumber;
    public String parish;
    public String age;
    public String fullName;

    public RegisterUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RegisterUser(String phoneNumber, String fullName, String parish, String age) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.parish = parish;
        this.age = age;
    }

    // [START RegisterUser_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("phoneNumber", phoneNumber);
        result.put("fullName", fullName);
        result.put("parish", parish);
        result.put("age", age);

        return result;
    }
    // [END RegisterUser_to_map]

}
// [END blog_user_class]


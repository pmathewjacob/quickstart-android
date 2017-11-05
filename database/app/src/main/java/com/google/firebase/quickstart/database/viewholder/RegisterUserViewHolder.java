package com.google.firebase.quickstart.database.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.RegisterUser;

public class RegisterUserViewHolder extends RecyclerView.ViewHolder {

    private TextView nameView;
    private TextView phoneNumberView;

    public RegisterUserViewHolder(View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.register_user_fName);
        phoneNumberView = itemView.findViewById(R.id.register_user_phoneNumber);
    }

    public void bindToPost(RegisterUser registerUser) {
        nameView.setText(registerUser.fullName);
        phoneNumberView.setText(registerUser.phoneNumber);
    }
}

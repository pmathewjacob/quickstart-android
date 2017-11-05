package com.google.firebase.quickstart.database.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.RegisterUserDetailActivity;
import com.google.firebase.quickstart.database.SignUpActivity;
import com.google.firebase.quickstart.database.models.RegisterUser;
import com.google.firebase.quickstart.database.viewholder.RegisterUserViewHolder;

public class RegisterUserFragment extends Fragment {

    private static final String TAG = "RegisterUserFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<RegisterUser, RegisterUserViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private Button button;

    public RegisterUserFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_users, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        button = rootView.findViewById(R.id.button_Register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch SignUpActivity
                startActivity(new Intent(getActivity(), SignUpActivity.class));
            }
        });
        mRecycler = rootView.findViewById(R.id.users_messages_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query registeredUsersQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<RegisterUser>()
                .setQuery(registeredUsersQuery, RegisterUser.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<RegisterUser, RegisterUserViewHolder>(options) {

            @Override
            public RegisterUserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new RegisterUserViewHolder(inflater.inflate(R.layout.item_register_user, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(RegisterUserViewHolder viewHolder, int position, final RegisterUser model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                Log.d(TAG, "postKey::" + postKey);
                viewHolder.bindToPost(model);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch RegisterUserDetailActivity
                        Intent intent = new Intent(getActivity(), RegisterUserDetailActivity.class);
                        intent.putExtra(RegisterUserDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference) {

        return databaseReference
                .child("register")
                .child(getUid())
                .limitToFirst(100);
    }

}

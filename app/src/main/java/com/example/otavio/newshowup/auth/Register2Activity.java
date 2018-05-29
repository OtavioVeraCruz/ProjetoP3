package com.example.otavio.newshowup.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.otavio.newshowup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register2Activity extends AppCompatActivity {

    @BindView(R.id.btn_pick_artist)Button btn_artista;
    @BindView(R.id.btn_pick_contratante)Button btn_contratante;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.constraint_register2)ConstraintLayout progressHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);

        Intent i=getIntent();
        String uid=i.getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Register2", "onAuthStateChanged:signed_in:" + user.getUid());
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Register2Activity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.saved_user_uid), user.getUid());
                    editor.apply();


                } else {
                    // User is signed out
                    Log.d("Register2", "onAuthStateChanged:signed_out");
                }
            }
        };
        btn_artista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register2Activity.this,ArtistRegistrationActivity.class));
            }
        });

        btn_contratante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register2Activity.this,ContratanteRegistrationActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

package com.example.otavio.newshowup.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    @BindView(R.id.progressbar)ProgressBar progressBar;
    @BindView(R.id.textViewVoce)TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);

        Intent i=getIntent();
        final int profile_exists=i.getIntExtra("profile_exists",0);

        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        progressHolder.setAlpha(0.4f);
        btn_contratante.setVisibility(View.INVISIBLE);
        btn_artista.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    if (profile_exists==1) {
                        Log.d("Register2", "onAuthStateChanged:signed_in:" + user.getUid());
                        progressBar.setVisibility(View.INVISIBLE);
                        progressHolder.setAlpha(1f);
                        textView.setVisibility(View.VISIBLE);
                        btn_contratante.setVisibility(View.VISIBLE);
                        btn_artista.setVisibility(View.VISIBLE);

                    }
                    else{
                        Log.d("Register2", "onAuthStateChanged:signed_in:" + user.getUid());
                    }

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
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

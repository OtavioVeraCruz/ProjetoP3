package com.example.otavio.newshowup.auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.otavio.newshowup.MainActivity;
import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.Snapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Context context;
    @BindView(R.id.email) EditText editEmail;
    @BindView(R.id.password)EditText editPassword;
    @BindView(R.id.btnLogin)Button login;
    @BindView(R.id.btnRegister)Button register;
    @BindView(R.id.constraint_login)ConstraintLayout constraintLayout;
    @BindView(R.id.progressbar)ProgressBar progressBar;
    private static final int MY_PERMISSIONS = 2;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth=Firebase.getmAuth();
        context=this;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editEmail.getText().toString().equals("")
                        &&!editEmail.getText().toString().equals("")){
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser!=null){
                        Log.d(TAG,"User exists!");

                    }
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    editEmail.setText(user.getEmail());
                    editPassword.setText("********");

                    //carregar dados do usuário
                    constraintLayout.setAlpha(0.4f);
                    progressBar.setVisibility(View.VISIBLE);

                    //disable user interaction
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Firebase.recoverFromUserUid(user.getUid(), new Runnable() {
                        @Override
                        public void run() {
                            String artista_id = Snapshot.getId_artista();
                            if(artista_id != null) {
                                Firebase.recover_artista(artista_id, context.getCacheDir().getPath(),new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout.setAlpha(1f);
                                        progressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

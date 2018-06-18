package com.example.otavio.newshowup.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.artista.HomeArtistaActivity;
import com.example.otavio.newshowup.contratante.HomeContratanteActivity;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotArtista;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    int permissionCheck;
    int permissionCamera;

    Context context;
    @BindView(R.id.email) EditText editEmail;
    @BindView(R.id.password)EditText editPassword;
    @BindView(R.id.btnLogin)Button login;
    @BindView(R.id.btnRegister)Button register;
    @BindView(R.id.constraint_login)ConstraintLayout constraintLayout;
    @BindView(R.id.progressbar)ProgressBar progressBar;
    private static final int MY_PERMISSIONS = 2;
    //private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth=FirebaseAuth.getInstance();
        context=this;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editEmail.getText().toString().equals("")
                        &&!editEmail.getText().toString().equals("")){
                    login.setEnabled(false);
                    register.setEnabled(false);
                        mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                        if (!task.isSuccessful()) {
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                            Toast.makeText(LoginActivity.this, "Login falhou!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.w(TAG, "signInWithEmail:succeed", task.getException());
                                            Toast.makeText(LoginActivity.this, "Login efetuado!",
                                                    Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });
                    }
                    else if(editEmail.getText().toString().equals("")){
                        Toast.makeText(getBaseContext(), "Digite o email", Toast.LENGTH_SHORT).show();
                    }
                    else if(editPassword.getText().toString().equals("")){
                        Toast.makeText(getBaseContext(), "Digite a senha", Toast.LENGTH_SHORT).show();
                    }
                    login.setEnabled(true);
                    register.setEnabled(true);
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
                    login.setAlpha(0.4f);
                    register.setAlpha(0.4f);
                    progressBar.setVisibility(View.VISIBLE);

                    //disable user interaction
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Firebase.recoverFromUserUid(user.getUid(),"Contratante" ,new Runnable() {
                        @Override
                        public void run() {
                            String contratante_id = SnapshotContratante.getId_contratante();
                            Log.d(TAG,"ID contratante: "+ contratante_id);
                            if(contratante_id != null) {
                                Firebase.recover_contratante(contratante_id,new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout.setAlpha(1f);
                                        progressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Intent intent = new Intent(LoginActivity.this, HomeContratanteActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                            }
                            else{
                                Log.d(TAG, "Não é contratante!");

                            }
                        }
                    });
                    Firebase.recoverFromUserUid(user.getUid(),"Artista" ,new Runnable() {
                        @Override
                        public void run() {
                            String artista_id = SnapshotArtista.getId_artista();
                            Log.d(TAG,"Id artista: "+ artista_id);
                            if(artista_id != null) {
                                Firebase.recover_artista(artista_id,new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout.setAlpha(1f);
                                        login.setAlpha(1f);
                                        register.setAlpha(1f);
                                        progressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Intent intent = new Intent(LoginActivity.this, HomeArtistaActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            else {
                                Log.d(TAG, "Não é artista!");

                            }
                        }
                    });
                    /*if (SnapshotArtista.getId_artista()==null &&SnapshotContratante.getId_contratante()==null){
                        constraintLayout.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Intent intent = new Intent(LoginActivity.this, Register2Activity.class)
                                .putExtra("profile_exists",1);
                        startActivity(intent);
                    }*/

                }
                else {
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

        permissionCheck = ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionCamera=ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.CAMERA);
        int granted = PackageManager.PERMISSION_GRANTED;

        if (permissionCheck==granted && permissionCamera==granted){
            Log.d("ACESS_GRANTED","Acess granted!");
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},MY_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("LOG_TAG", "Permission has been granted by user");
                }
                else {
                    Log.d("LOG_TAG", "Permission has been denied!");
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

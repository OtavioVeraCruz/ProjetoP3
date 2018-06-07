package com.example.otavio.newshowup.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.email)EditText email;
    @BindView(R.id.password)EditText password;
    @BindView(R.id.confirmationPassword)EditText confirmPassword;
    @BindView(R.id.btnRegister)Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Register", "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                    Intent intent = new Intent(RegisterActivity.this, Register2Activity.class).
                            putExtra("profile_exists",1);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d("Register", "onAuthStateChanged:signed_out");
                }
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister.setEnabled(false);

                if(!email.getText().toString().equals("") &&
                        !password.getText().toString().equals("") &&
                        !confirmPassword.getText().toString().equals("") &&
                        password.getText().toString().equals(confirmPassword.getText().toString())) {

                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                static final String TAG = "RegisterActivity";

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Register", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Cadastro falhou! O email deve ser válido e a senha maior que 6 caracteres.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d(TAG,"Passo 1 completo!");
                                    }
                                }
                            });
                }
                else if(email.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Digite o email", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Digite a senha", Toast.LENGTH_SHORT).show();
                }
                else if(confirmPassword.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Digite a confirmação da senha", Toast.LENGTH_SHORT).show();
                }
                else if(!confirmPassword.getText().toString().equals(password.getText().toString())){
                    Toast.makeText(getBaseContext(), "A senha deve ser igual a confirmação", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegister.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

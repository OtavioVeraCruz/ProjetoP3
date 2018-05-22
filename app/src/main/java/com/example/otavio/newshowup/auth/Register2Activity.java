package com.example.otavio.newshowup.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register2Activity extends AppCompatActivity {

    @BindView(R.id.btn_pick_artist)Button btn_artista;
    @BindView(R.id.btn_pick_contratante)Button btn_contratante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Register2Activity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.saved_user_uid), Firebase.getmAuth().getUid());
        editor.apply();

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
    protected void onStop() {
        super.onStop();
    }
}

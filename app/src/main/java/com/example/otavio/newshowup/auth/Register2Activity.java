package com.example.otavio.newshowup.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.otavio.newshowup.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register2Activity extends AppCompatActivity {

    @BindView(R.id.btn_pick_artist)Button btn_artista;
    @BindView(R.id.btn_pick_contratante)Button btn_contratante;
    @BindView(R.id.constraint_register2)ConstraintLayout progressHolder;
    @BindView(R.id.progressbar)ProgressBar progressBar;
    @BindView(R.id.textViewVoce)TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);

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
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

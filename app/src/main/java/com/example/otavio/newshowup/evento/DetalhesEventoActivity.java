package com.example.otavio.newshowup.evento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalhesEventoActivity extends AppCompatActivity {

    @BindView(R.id.slide_imgs_evento)ViewPager viewPager;
    @BindView(R.id.event_title)TextView title;
    @BindView(R.id.event_description)TextView description;
    @BindView(R.id.event_location)TextView location;
    @BindView(R.id.event_date)TextView date;
    @BindView(R.id.event_price)TextView price;
    @BindView(R.id.btn_candidatar_evento)Button candidatar;
    @BindView(R.id.progressBarDetails)ProgressBar progressBar;
    @BindView(R.id.progressbarHolderEventDetails)ConstraintLayout progressBarHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);
        ButterKnife.bind(this);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent intent=getIntent();
        String id_evento=intent.getStringExtra("id_contratante");
        final ImageSliderPager sliderPager;

        progressBarHolder.setAlpha(0.4f);
        progressBar.setVisibility(View.VISIBLE);
        //disable user interaction
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final Firebase.Evento evento = (Firebase.Evento) intent.getSerializableExtra("evento");
        if (evento!=null) {
            sliderPager = new ImageSliderPager(this, evento.fotos);
            viewPager.setAdapter(sliderPager);
            String titulo = title.getText().toString() +" "+ evento.nome;
            title.setText(titulo);
            description.setText(evento.descricao);
            location.setText(evento.cidade);
            date.setText(evento.data);
            price.setText(evento.faixa_preco);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarHolder.setAlpha(1f);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        },3000);

        candidatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

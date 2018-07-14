package com.example.otavio.newshowup.evento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.artista.HomeArtistaActivity;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotArtista;
import com.example.otavio.newshowup.utils.SnapshotEvento;

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
        final ImageSliderPager sliderPager;

        viewPager.setAlpha(0.4f);
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
                viewPager.setAlpha(1f);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        },3000);


        String keyaux="";
        if (SnapshotEvento.getEvento().candidatos!=null){
            for (String key : SnapshotEvento.getEvento().candidatos){
                if (key.equals(SnapshotArtista.getId_artista())){
                    keyaux=key;
                }
            }
        }
        if (!keyaux.equals("")){

            candidatar.setBackgroundColor(getResources().getColor(R.color.red));
            final String cancelar="Cancelar candidatura";
            candidatar.setText(cancelar);
            candidatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    candidatar.setEnabled(false);
                    assert evento != null;
                    Firebase.removeCandidatura(evento.id, SnapshotArtista.getId_artista(), new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(DetalhesEventoActivity.this,"Candidatura removida com sucesso!"
                                        ,Toast.LENGTH_LONG).show();
                            candidatar.setBackgroundColor(getResources().getColor(R.color.mdtp_accent_color));
                            String candidatura="Candidatar-se";
                            candidatar.setText(candidatura);
                            candidatar.setEnabled(true);
                            finish();
                            startActivity(new Intent(DetalhesEventoActivity.this, HomeArtistaActivity.class));

                        }
                    });
                }
            });
        }else {
            candidatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assert evento != null;
                    Firebase.candidatarEvento(evento.id, SnapshotArtista.getId_artista(), new Runnable() {
                        @Override
                        public void run() {

                          Toast.makeText(DetalhesEventoActivity.this,"Candidatura realizada com sucesso!"
                                        ,Toast.LENGTH_LONG).show();
                            candidatar.setBackgroundColor(getResources().getColor(R.color.red));
                            String cancelar="Cancelar candidatura";
                            candidatar.setText(cancelar);
                            finish();
                            startActivity(new Intent(DetalhesEventoActivity.this, HomeArtistaActivity.class));

                        }
                    });
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

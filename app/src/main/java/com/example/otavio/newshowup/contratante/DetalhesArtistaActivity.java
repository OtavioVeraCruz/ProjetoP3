package com.example.otavio.newshowup.contratante;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.example.otavio.newshowup.youtube.YoutubeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.otavio.newshowup.R.color.colorPrimary;

public class DetalhesArtistaActivity extends AppCompatActivity {
    @BindView(R.id.image_artista_detalhes)ImageView image_artista;
    @BindView(R.id.text_nome_artista)TextView nome_artista;
    @BindView(R.id.text_genero)TextView genero;
    @BindView(R.id.text_contato)TextView contato;
    @BindView(R.id.text_faixa_preco_artista)TextView faixa_preco;
    @BindView(R.id.btn_youtube_playlist)Button youtube_playlist;
    @BindView(R.id.text_canalyoutube)TextView canal_youtube;
    @BindView(R.id.text_cidade_artista)TextView cidade;
    @BindView(R.id.btnSelecionarArtista)Button btn_selecionar;
    DatabaseReference notificationRef=Firebase.mDatabaseRef.child("Notification");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_artista);
        ButterKnife.bind(this);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent=getIntent();
        final Firebase.Artista artista= (Firebase.Artista) intent.getSerializableExtra("artista");
        LoadImg.loadSimpleImage(artista.foto,image_artista,this);
        String nome=nome_artista.getText().toString()+artista.nome;
        nome_artista.setText(nome);
        String genero_mus=genero.getText().toString()+artista.dadosArtista.genero;
        genero.setText(genero_mus);
        String city=cidade.getText().toString()+": "+artista.dadosArtista.cidade;
        cidade.setText(city);
        String contato_art=contato.getText().toString()+artista.dadosArtista.telefone;
        contato.setText(contato_art);
        String preco=faixa_preco.getText().toString()+artista.dadosArtista.faixa_preco;
        faixa_preco.setText(preco);

        if (artista.youtube_channel!=null) {
            youtube_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (artista.youtube_channel != null) {
                        Intent i = new Intent(DetalhesArtistaActivity.this, YoutubeActivity.class);
                        Log.d("playlist", artista.youtube_channel);
                        i.putExtra("playlist", artista.youtube_channel);
                        startActivity(i);
                    } else {
                        Log.d("Error", "Error youtube!");
                    }
                }
            });
        }
        else {
            youtube_playlist.setVisibility(View.INVISIBLE);
            canal_youtube.setVisibility(View.INVISIBLE);
        }
        final String id= SnapshotContratante.getId_contratante();

        btn_selecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notification_id=notificationRef.push().getKey();

                btn_selecionar.setEnabled(false);
                HashMap<String,String>notificationData=new HashMap<>();
                notificationData.put("from",id);
                notificationData.put("to",artista.id);

                assert notification_id != null;
                notificationRef.child(notification_id).setValue(notificationData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            btn_selecionar.setText("Enviado!");
                            btn_selecionar.setBackgroundColor(getResources().getColor(colorPrimary));
                        }
                    }
                });

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

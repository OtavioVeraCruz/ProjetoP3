package com.example.otavio.newshowup.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SearchArtistaService extends IntentService {

    public static final String FEED_LOADED = "com.example.otavio.newshowup.contratante.action.FEED_LOADED";
    Context context;
    public SearchArtistaService(String name) {
        super(name);
    }

    public SearchArtistaService() {
        super("SearchArtistaService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String q=intent.getStringExtra("query");
        if (q.equals("query")){
            String[]query=q.split("/");
            final String genero=query[0];
            final String preco=query[1];
            final String cidade=query[2];
            context=this;

            Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                        assert artista != null;
                        //3 casos
                        if (artista.dadosArtista.genero.equals(genero)&&!genero.equals(ArrayString.generos[0])&&
                                artista.dadosArtista.faixa_preco.equals(preco)&&!preco.equals(ArrayString.faixas_preco[0])&&
                                cidade.equals(artista.dadosArtista.cidade)&&!cidade.equals(ArrayString.cidades[0])){
                            SnapshotContratante.artistas.add(artista);

                        }
                        //genero e cidade
                        else if (artista.dadosArtista.genero.equals(genero)&&!genero.equals(ArrayString.generos[0])
                                &&preco.equals(ArrayString.faixas_preco[0])&&cidade.equals(artista.dadosArtista.cidade)&&
                                !cidade.equals(ArrayString.cidades[0])){
                            SnapshotContratante.artistas.add(artista);
                        }
                        //preço e cidade
                        else if (genero.equals(ArrayString.generos[0])&&!preco.equals(ArrayString.faixas_preco[0])&&
                                artista.dadosArtista.faixa_preco.equals(preco)&&cidade.equals(artista.dadosArtista.cidade)&&
                                !cidade.equals(ArrayString.cidades[0])){
                            SnapshotContratante.artistas.add(artista);
                        }
                        //genero
                        else if (artista.dadosArtista.genero.equals(genero)&&!genero.equals(ArrayString.generos[0])
                                && preco.equals(ArrayString.faixas_preco[0]) &&
                                cidade.equals(ArrayString.cidades[0])){
                            SnapshotContratante.artistas.add(artista);
                        }
                        //preço
                        else if (genero.equals(ArrayString.generos[0])&&artista.dadosArtista.faixa_preco.equals(preco)
                                &&!preco.equals(ArrayString.faixas_preco[0])&&cidade.equals(ArrayString.cidades[0])){
                            SnapshotContratante.artistas.add(artista);
                        }
                        //cidade
                        else {
                            SnapshotContratante.artistas.add(artista);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED_LOADED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
      else{
            final String genero=intent.getStringExtra("genero");
            Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                        assert artista != null;
                        if (artista.dadosArtista.genero.equals(genero)){
                            Firebase.Artista aux = null;
                            for (int i=0;i<SnapshotContratante.artistas.size();i++) {
                                if (SnapshotContratante.artistas.get(i).id.equals(artista.id)){
                                    aux=artista;
                                }
                            }
                            if (aux==null){
                                SnapshotContratante.artistas.add(artista);
                            }
                        }

                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED_LOADED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}

package com.example.otavio.newshowup.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        if (q.equalsIgnoreCase("query")){
            String search=intent.getStringExtra("search");
            String[]query=search.split("/");
            Log.d("Busca",search);
            for (int i=0;i<query.length-1;i++){
                Log.d("Busca",query[i]);
            }
            final String genero=query[0];
            final String preco=query[1];
            final String cidade=query[2];
            context=this;

            //3
            if (!preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                    !genero.equals(ArrayString.generos[0])){
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.faixa_preco.equals(preco)&&artista.dadosArtista.genero
                                    .equals(genero)&&artista.dadosArtista.cidade.equals(cidade)&&!existe(SnapshotContratante.artistas,artista.id)){
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
            //Preço e cidade
            else if (!preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                    genero.equals(ArrayString.generos[0])){
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.faixa_preco.equals(preco)&&artista.dadosArtista.cidade.
                                    equals(cidade)&&!existe(SnapshotContratante.artistas,artista.id)){
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
            //Preço e genero
            else if (!preco.equals(ArrayString.faixas_preco[0])&&cidade.equals(ArrayString.cidades[0])&&
                    !genero.equals(ArrayString.generos[0])){
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.faixa_preco.equals(preco)&&artista.dadosArtista.genero
                                    .equals(genero)&&!existe(SnapshotContratante.artistas,artista.id)){
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
            //Cidade e genero
            else if (preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                    !genero.equals(ArrayString.generos[0])){
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.genero.equals(genero)&&artista.dadosArtista.cidade.
                                    equals(cidade)&&!existe(SnapshotContratante.artistas,artista.id)){
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
            //Preço
            else if (!preco.equals(ArrayString.faixas_preco[0])&&cidade.equals(ArrayString.cidades[0])&&
                    genero.equals(ArrayString.generos[0])){
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.faixa_preco.equals(preco)&&
                                    !existe(SnapshotContratante.artistas,artista.id)){
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
            //Cidade
            else if (preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                    genero.equals(ArrayString.generos[0])){
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.cidade.equals(cidade)&&!existe(SnapshotContratante.
                                    artistas,artista.id)){
                                SnapshotContratante.artistas.add(artista);
                            }
                        }
                       LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED_LOADED));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }//genero
            else {
                Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Firebase.Artista artista=data.getValue(Firebase.Artista.class);
                            assert artista != null;
                            if (artista.dadosArtista.genero.equals(genero)&&!existe(SnapshotContratante
                                    .artistas,artista.id)){
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

    public boolean existe(ArrayList<Firebase.Artista> artistas, String id){
        boolean retorno=false;

        for (int i=0;i<artistas.size();i++){
            if (artistas.get(i).id.equals(id)){
                retorno=true;
            }
        }

        return retorno;
    }
}

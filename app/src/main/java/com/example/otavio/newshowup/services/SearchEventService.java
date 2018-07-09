package com.example.otavio.newshowup.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotArtista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchEventService extends IntentService {
    public static final String FEED = "com.example.otavio.newshowup.artista.action.FEED";
    Context context;
    public SearchEventService(String name) {
        super(name);
    }
    public SearchEventService() {
        super("SearchEventService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String[]query=intent.getStringExtra("search").split("#");
        final String preco=query[0];
        final String cidade=query[1];
        final String data_evento=query[2];
        context=this;
        //3
        if (!preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                !data_evento.equals("qualquer")){
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.faixa_preco.equals(preco)&&evento.data.equals(data_evento)&&
                                evento.cidade.equals(cidade)&&!existe(SnapshotArtista.eventos,evento.id)){

                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Preço e cidade
        else if (!preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                data_evento.equals("qualquer")){
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.faixa_preco.equals(preco)&&evento.cidade.equals(cidade)&&
                                !existe(SnapshotArtista.eventos,evento.id)){
                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Preço e data
        else if (!preco.equals(ArrayString.faixas_preco[0])&&cidade.equals(ArrayString.cidades[0])&&
                !data_evento.equals("qualquer")){
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.faixa_preco.equals(preco)&&evento.data.equals(data_evento)
                                &&!existe(SnapshotArtista.eventos,evento.id)){
                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Cidade e data
        else if (preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                !data_evento.equals("qualquer")){
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.data.equals(data_evento)&&evento.cidade.equals(cidade)
                                &&!existe(SnapshotArtista.eventos,evento.id)){
                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Preço
        else if (!preco.equals(ArrayString.faixas_preco[0])&&cidade.equals(ArrayString.cidades[0])&&
                data_evento.equals("qualquer")){
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.faixa_preco.equals(preco)&&!existe(SnapshotArtista.eventos,evento.id)){
                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Cidade
        else if (preco.equals(ArrayString.faixas_preco[0])&&!cidade.equals(ArrayString.cidades[0])&&
                data_evento.equals("qualquer")){
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.cidade.equals(cidade)&&!existe(SnapshotArtista.eventos,evento.id)){
                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }//Data
        else {
            Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                        assert evento != null;
                        if (evento.data.equals(data_evento)){
                            SnapshotArtista.eventos.add(evento);
                        }
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public boolean existe(ArrayList<Firebase.Evento>eventos,String id){
        boolean retorno=false;

        for (int i=0;i<eventos.size();i++){
            if (eventos.get(i).id.equals(id)){
                retorno=true;
            }
        }

        return retorno;
    }
}

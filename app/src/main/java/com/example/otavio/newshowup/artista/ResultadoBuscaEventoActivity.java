package com.example.otavio.newshowup.artista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toolbar;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.evento.EventoAdapter;
import com.example.otavio.newshowup.utils.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoBuscaEventoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.recycler_search_event)RecyclerView recyclerView;
    String query;
    ArrayList<Firebase.Evento>eventos;
    EventoAdapter eventoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca_evento);
        ButterKnife.bind(this);
        Intent i = getIntent();
        query=i.getStringExtra("search");
        recyclerView=findViewById(R.id.recycler_search_event);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));

        eventos=new ArrayList<>();

        eventoAdapter=new EventoAdapter(this,eventos);

        getEventos();

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Intent intent=new Intent(this, SearchEventService.class);
        intent.putExtra("search",query);
        startService(intent);*/
    }
    public void getEventos(){
        Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data :dataSnapshot.getChildren()){
                    Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                    assert evento != null;
                    Log.d("REsult", "Evento "+evento.nome);
                    eventos.add(evento);
                }
                recyclerView.setAdapter(eventoAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

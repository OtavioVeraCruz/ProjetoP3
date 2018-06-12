package com.example.otavio.newshowup.artista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.evento.EventoViewHolder;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoBuscaEventoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.recycler_search_event)RecyclerView recyclerView;
    String query;

    private FirebaseRecyclerAdapter<Firebase.Evento, EventoViewHolder> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca_evento);
        ButterKnife.bind(this);
        Intent i = getIntent();
        query=i.getStringExtra("search");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query q=Firebase.mDatabaseRef.child("Evento");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FirebaseRecyclerAdapter<Firebase.Evento, EventoViewHolder>(
                Firebase.Evento.class, R.layout.item_evento, EventoViewHolder.class, q) {
            @Override
            protected void populateViewHolder(EventoViewHolder viewHolder, Firebase.Evento model, int position) {
                viewHolder.cidade.setText(model.cidade);
                viewHolder.data.setText(model.data);
                viewHolder.data.setText(model.nome);
                viewHolder.faixa_preco.setText(model.faixa_preco);
                String desc=model.descricao;
                viewHolder.data.setText(desc);
                String url=model.fotos.get(0);
                LoadImg.loadImage(url,viewHolder.imageView,ResultadoBuscaEventoActivity.this);
            }

            @Override
            public void onChildChanged(EventType type, DataSnapshot snapshot, int index, int oldIndex) {
                super.onChildChanged(type, snapshot, index, oldIndex);
                recyclerView.scrollToPosition(index);
            }
        };
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Intent intent=new Intent(this, SearchEventService.class);
        intent.putExtra("search",query);
        startService(intent);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}

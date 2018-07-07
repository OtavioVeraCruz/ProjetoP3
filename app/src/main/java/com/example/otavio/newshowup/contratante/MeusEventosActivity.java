package com.example.otavio.newshowup.contratante;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.evento.EventoViewHolder;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeusEventosActivity extends AppCompatActivity {
    @BindView(R.id.recycler_meus_eventos)RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Firebase.Evento, EventoViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos);
        ButterKnife.bind(this);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query q=Firebase.mDatabaseRef.child("Evento").orderByChild("id_contratante").startAt(SnapshotContratante.getId_contratante())
                .endAt(SnapshotContratante.getId_contratante());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FirebaseRecyclerAdapter<Firebase.Evento, EventoViewHolder>(
                Firebase.Evento.class, R.layout.item_evento, EventoViewHolder.class, q) {
            @Override
            protected void populateViewHolder(EventoViewHolder viewHolder, final Firebase.Evento model, int position) {

                if (model!=null) {
                    viewHolder.cidade.setText(model.cidade);
                    viewHolder.data.setText(model.data);
                    viewHolder.title.setText(model.nome);
                    viewHolder.faixa_preco.setText(model.faixa_preco);
                    if (model.fotos.size()>0) {
                        String url = model.fotos.get(0);
                        LoadImg.loadImage(url, viewHolder.imageView, MeusEventosActivity.this);
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MeusEventosActivity.this, EditarEventoActivity.class)
                                    .putExtra("id_evento", model.id).putExtra("evento", model));
                        }
                    });

                    viewHolder.imageViewDelete.setVisibility(View.VISIBLE);
                    viewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Firebase.removeEvento(model.id, new Runnable() {
                            @Override
                            public void run() {
                                if (Firebase.getIsEventRemoved()){
                                    Toast.makeText(MeusEventosActivity.this,"Evento removido!"
                                            ,Toast.LENGTH_LONG).show();
                                    Firebase.setIsEventRemoved(false);
                                }
                                else{
                                    Toast.makeText(MeusEventosActivity.this,"Evento não pode ser removido!"
                                            ,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        }
                    });
                }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter!=null) {
            mAdapter.cleanup();
        }
        finish();
    }
}

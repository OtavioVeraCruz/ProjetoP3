package com.example.otavio.newshowup.contratante;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.services.SearchArtistaService;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotContratante;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoBuscaContratanteActivity extends AppCompatActivity {

    @BindView(R.id.rv_resultadocontratante)RecyclerView recyclerView;
    @BindView(R.id.text_resultado)TextView result;
    String query;
    String genero;
    ArtistaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca_contratante);
        ButterKnife.bind(this);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i=getIntent();
        query=i.getStringExtra("query");
        genero=i.getStringExtra("genero");
        if (query.equals("genero")){
            result.setText(result.getText()+" gênero: "+genero);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(ResultadoBuscaContratanteActivity.this);
        layoutManager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ArtistaAdapter(SnapshotContratante.artistas,ResultadoBuscaContratanteActivity.this);

    }

    protected void onStart() {
        super.onStart();
        Intent load_feed=new Intent(this,SearchArtistaService.class);
        load_feed.putExtra("query",query).putExtra("genero",genero);
        startService(load_feed);

    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f = new IntentFilter(SearchArtistaService.FEED_LOADED);
        LocalBroadcastManager.getInstance(getApplicationContext()).
                registerReceiver(onDownloadCompleteEvent, f);
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).
                unregisterReceiver(onDownloadCompleteEvent);
    }

    class ExibirFeed extends AsyncTask<Void, Void, ArrayList<Firebase.Artista>> {

        @Override
        protected  ArrayList<Firebase.Artista> doInBackground(Void... voids) {
            return SnapshotContratante.artistas;
        }

        @Override
        protected void onPostExecute( ArrayList<Firebase.Artista> c) {
            if (c != null) {
                SnapshotContratante.artistas=c;
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        }
    }
    private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            new ExibirFeed().execute();
        }
    };
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
        SnapshotContratante.artistas=new ArrayList<>();
        adapter=null;
        finish();
    }


}

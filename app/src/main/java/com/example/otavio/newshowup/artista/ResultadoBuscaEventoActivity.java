package com.example.otavio.newshowup.artista;

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
import android.view.View;
import android.widget.ProgressBar;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.services.SearchEventService;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotArtista;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoBuscaEventoActivity extends AppCompatActivity {

    @BindView(R.id.recycler_search_event)RecyclerView recyclerView;
    @BindView(R.id.progressbar)ProgressBar progressBar;
    String query;
    EventoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca_evento);
        ButterKnife.bind(this);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent i = getIntent();
        query=i.getStringExtra("search");
        LinearLayoutManager layoutManager = new LinearLayoutManager(ResultadoBuscaEventoActivity.this);
        layoutManager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    protected void onStart() {
        super.onStart();
        Intent load_feed=new Intent(this,SearchEventService.class);
        load_feed.putExtra("search",query);
        startService(load_feed);

    }
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter f = new IntentFilter(SearchEventService.FEED);
        LocalBroadcastManager.getInstance(getApplicationContext()).
                registerReceiver(onDownloadCompleteEvent, f);
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).
                unregisterReceiver(onDownloadCompleteEvent);
    }

    class ExibirFeed extends AsyncTask<Void, Void, ArrayList<Firebase.Evento>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected  ArrayList<Firebase.Evento> doInBackground(Void... voids) {
            return SnapshotArtista.eventos;
        }

        @Override
        protected void onPostExecute( ArrayList<Firebase.Evento> c) {
            if (c != null) {
                adapter=new EventoAdapter(ResultadoBuscaEventoActivity.this,c);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            new ExibirFeed().execute();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter=null;
        SnapshotArtista.eventos=new ArrayList<>();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

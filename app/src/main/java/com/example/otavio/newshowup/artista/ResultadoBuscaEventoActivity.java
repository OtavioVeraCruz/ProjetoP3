package com.example.otavio.newshowup.artista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.example.otavio.newshowup.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoBuscaEventoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.recycler_search_event)RecyclerView recyclerView;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca_evento);
        ButterKnife.bind(this);
        Intent i = getIntent();
        query=i.getStringExtra("search");

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Intent intent=new Intent(this, SearchEventService.class);
        intent.putExtra("search",query);
        startService(intent);*/
    }
}

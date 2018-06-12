package com.example.otavio.newshowup.artista;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.auth.LoginActivity;
import com.example.otavio.newshowup.evento.DetalhesEventoActivity;
import com.example.otavio.newshowup.evento.EventoViewHolder;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotArtista;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeArtistaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG ="HomeArtista" ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ImageView img_profile;
    @BindView(R.id.recycler_artist) RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<Firebase.Evento, EventoViewHolder> mAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_artista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        setUpSideMenu(v);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query q=Firebase.mDatabaseRef.child("Evento").limitToLast(50);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FirebaseRecyclerAdapter<Firebase.Evento, EventoViewHolder>(
                Firebase.Evento.class, R.layout.item_evento, EventoViewHolder.class, q) {
            @Override
            protected void populateViewHolder(EventoViewHolder viewHolder, final Firebase.Evento model, int position) {
                viewHolder.cidade.setText(model.cidade);
                viewHolder.data.setText(model.data);
                viewHolder.title.setText(model.nome);
                viewHolder.faixa_preco.setText(model.faixa_preco);
                String url=model.fotos.get(0);
                LoadImg.loadImage(url,viewHolder.imageView,HomeArtistaActivity.this);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeArtistaActivity.this,DetalhesEventoActivity.class)
                                .putExtra("id_evento",model.id).putExtra("evento",model));
                    }
                });

            }

            @Override
            public void onChildChanged(EventType type, DataSnapshot snapshot, int index, int oldIndex) {
                super.onChildChanged(type, snapshot, index, oldIndex);
                recyclerView.scrollToPosition(index);
            }
        };
        recyclerView.setAdapter(mAdapter);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(HomeArtistaActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };


    }

    public void setUpSideMenu(View v){
        String url_foto;
        String nome;
        img_profile=v.findViewById(R.id.imageView_sideNav);
        if (SnapshotArtista.getArtista()!=null){
            url_foto=SnapshotArtista.getArtista().foto;
            nome = SnapshotArtista.getArtista().nome;
            LoadImg.loadImage(url_foto,img_profile,this);
            TextView text_nome=v.findViewById(R.id.profile_name);
            text_nome.setText(nome);
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_artista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(this,BuscarEventoActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_artista) {

        } else if (id==R.id.edit_perfil){
            startActivity(new Intent(this,PerfilArtistaActivity.class));
        } else if (id == R.id.eventos_fav) {
            startActivity(new Intent(this,FavoritesEventsActivity.class));

        } else if (id == R.id.logout) {
            (this).finish();
            Firebase.logout();
            SnapshotArtista.reset();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

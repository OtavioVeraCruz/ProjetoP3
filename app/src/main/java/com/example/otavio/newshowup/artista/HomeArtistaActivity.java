package com.example.otavio.newshowup.artista;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.otavio.newshowup.evento.EventoAdapter;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotArtista;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class HomeArtistaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG ="HomeArtista" ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ImageView img_profile;

    ArrayList<Firebase.Evento>eventos;

    RecyclerView recyclerView;
    EventoAdapter eventoAdapter;
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
        img_profile=v.findViewById(R.id.imageView_sideNav);
        String url_foto;
        String nome;
        if (SnapshotArtista.getArtista()!=null){
            url_foto=SnapshotArtista.getArtista().foto;
            nome = SnapshotArtista.getArtista().nome;
            LoadImg.loadImage(url_foto,img_profile,this);
            TextView text_nome=v.findViewById(R.id.profile_name);
            text_nome.setText(nome);
        }

        recyclerView=findViewById(R.id.recycler_artist);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));

        eventos=new ArrayList<>();

        eventoAdapter=new EventoAdapter(this,eventos);
        recyclerView.setAdapter(eventoAdapter);
        getEventos();

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

    public void getEventos(){
        Firebase.mDatabaseRef.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data :dataSnapshot.getChildren()){
                    Firebase.Evento evento=data.getValue(Firebase.Evento.class);
                    Log.d(TAG, "Evento "+evento.nome);
                    eventos.add(evento);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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



    class CarregaFeedTask extends AsyncTask<String,Firebase.Evento,ArrayList<Firebase.Evento>>{

        @Override
        protected ArrayList<Firebase.Evento> doInBackground(String... strings) {
            return null;
        }
    }
}

package com.example.otavio.newshowup.contratante;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.auth.LoginActivity;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeContratanteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.fabAddEvent)FloatingActionButton fabAddEvent;
    @BindView(R.id.fabEvents)FloatingActionButton fabEvents;
    @BindView(R.id.cardview_add_event)CardView cardViewAdd;
    @BindView(R.id.cardview_events)CardView cardViewEvents;
    @BindView(R.id.layoutFabAddEvents)LinearLayout linearLayout_add;
    @BindView(R.id.layoutFabEvents)LinearLayout linearLayout_events;
    @BindView(R.id.recycler_contratatnte)RecyclerView recyclerView;
    private static boolean fab_status=false;
    ImageView img_profile;
    TextView nome_contratante;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_contratante);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<Firebase.Artista>artistas=new ArrayList<>();
        Firebase.mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren())
                artistas.add(data.getValue(Firebase.Artista.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArtistaAdapter adapter=new ArtistaAdapter(artistas,this);
        recyclerView.setAdapter(adapter);

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);

        img_profile=view.findViewById(R.id.image_profile_contratante);
        nome_contratante=view.findViewById(R.id.text_nome_contratante);
        String url;
        if (SnapshotContratante.getContratante()!=null) {
            url = SnapshotContratante.getContratante().foto;
            LoadImg.loadImage(url, img_profile, this);
            String nome = SnapshotContratante.getContratante().nome;
            nome_contratante.setText(nome);
        }
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("HomeContratante", "onAuthStateChanged:signed_in:" + user.getUid());


                } else {
                    // User is signed out
                    Log.d("HomeContratante", "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(HomeContratanteActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fab_status){
                    openMenuFab();
                }
                else{
                    closeMenuFab();
                }

            }
        });
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeContratanteActivity.this,CriarEventoActivity.class));
            }
        });

        fabEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeContratanteActivity.this,MeusEventosActivity.class));
            }
        });

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeContratanteActivity.this,CriarEventoActivity.class));
            }
        });

        cardViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeContratanteActivity.this,MeusEventosActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
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
        getMenuInflater().inflate(R.menu.home_contratante, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search_artist) {
            startActivity(new Intent(HomeContratanteActivity.this,BuscarArtistaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home_contratante) {


        }else if (id == R.id.edit_perfil_contratante) {
            startActivity(new Intent(HomeContratanteActivity.this,PerfilContratanteActivity.class));

        }else if (id == R.id.nav_favorites) {
            startActivity(new Intent(HomeContratanteActivity.this,ArtistasFavoritosActivity.class));

        }

        else if (id == R.id.nav_logout) {
            finish();
            Firebase.logout();
            SnapshotContratante.reset();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openMenuFab(){
        linearLayout_add.setVisibility(View.VISIBLE);
        linearLayout_events.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_close_black_24dp);

        fab_status=true;
    }
    public void closeMenuFab(){
        linearLayout_add.setVisibility(View.INVISIBLE);
        linearLayout_events.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab_status=false;
    }
}

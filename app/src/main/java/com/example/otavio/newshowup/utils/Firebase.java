package com.example.otavio.newshowup.utils;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.otavio.newshowup.services.MyFirebaseInstanceIDService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Firebase {

    private static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    public static DatabaseReference mDatabaseRef=mDatabase.getReference();
    private static FirebaseStorage mStorage=FirebaseStorage.getInstance();
    private static StorageReference mStorageRef=mStorage.getReference();
    private static String TAG="Firebase";


    @IgnoreExtraProperties
    public static class Artista{
        public String id;
        public String nome;
        public String foto;
        public DadosArtista dadosArtista;
        String token;
        public String uid;
        public String youtube_channel;

        public Artista(){}
        public Artista(String id,String nome,String foto,DadosArtista dadosArtista,String token,
                       String uid,String youtube_channel){
            this.id=id;
            this.nome=nome;
            this.foto=foto;
            this.dadosArtista=dadosArtista;
            this.token=token;
            this.uid=uid;
            this.youtube_channel=youtube_channel;
        }

    }
    @IgnoreExtraProperties
    public static class DadosArtista{
        public String telefone;
        public String cidade;
        public String estado;
        public String genero;
        public String faixa_preco;
        public DadosArtista(){}
        public DadosArtista(  String telefone,String cidade,String estado,String genero,String faixa_preco){
            this.telefone=telefone;
            this.cidade=cidade;
            this.estado=estado;
            this.genero=genero;
            this.faixa_preco=faixa_preco;
        }
    }

    @IgnoreExtraProperties
    public static class Contratante{
        public String id;
        public String nome;
        public String foto;
        public String estado;
        public String cidade;
        public String token;
        public String uid;
        public String telefone;

        public Contratante(){}
        public Contratante(String id,String nome,String foto,String token,
                       String uid,String telefone,String estado,String cidade){
            this.id=id;
            this.nome=nome;
            this.foto=foto;
            this.token=token;
            this.uid=uid;
            this.telefone=telefone;
            this.estado=estado;
            this.cidade=cidade;
        }


    }

    @IgnoreExtraProperties
    public static class Evento{
        public String id;
        public String id_contratante;
        public String nome;
        public String descricao;
        public ArrayList<String> instrumentos;
        public String faixa_preco;
        public ArrayList<String>fotos;
        public String cidade;
        public String data;

        public Evento() {
        }
        public Evento(String id, String id_contratante, String nome,String descricao,ArrayList<String> instrumentos,
                      String faixa_preco, ArrayList<String> fotos,String cidade,String data) {
            this.id = id;
            this.id_contratante = id_contratante;
            this.nome = nome;
            this.descricao = descricao;
            this.instrumentos = instrumentos;
            this.faixa_preco = faixa_preco;
            this.fotos = fotos;
            this.cidade=cidade;
            this.data=data;
        }

    }


    public static void recover_artista(String artista_id, final Runnable runnable){
        TaskCompletionSource<Boolean> getArtistaSource = new TaskCompletionSource<>();
        Task getArtista = getArtistaSource.getTask();
        getArtista(artista_id,getArtistaSource);
        final Task<Void> task;
        task = Tasks.whenAll(getArtista);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (SnapshotArtista.getArtista()!=null){
                    runnable.run();
                }
                else {
                    runnable.run();
                }
            }
        });
    }
    public static void recover_contratante(String contratante_id, final Runnable runnable) {
        TaskCompletionSource<Boolean> getContratanteSource = new TaskCompletionSource<>();
        Task getContratante = getContratanteSource.getTask();
        getContratante(contratante_id,getContratanteSource);
        final Task<Void> task;
        task = Tasks.whenAll(getContratante);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (SnapshotContratante.getContratante()!=null){
                    runnable.run();
                }
                else {
                    runnable.run();
                }
            }
        });
    }
    public static void getArtista(final String id, final TaskCompletionSource<Boolean> dbSource){
        Query query = mDatabaseRef.child("Artista");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Artista artista = data.getValue(Artista.class);
                    assert artista != null;
                    if(artista.id.equals(id)) {
                        SnapshotArtista.setArtista(artista);
                        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                        myFirebaseInstanceIDService.onTokenRefresh();
                    }

                }
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public static void getArtistaT(final String id, final Runnable onload){
    mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Artista artista = data.getValue(Artista.class);
                    assert artista != null;
                    if(artista.id.equals(id)) {
                        SnapshotArtista.setArtista(artista);
                        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                        myFirebaseInstanceIDService.onTokenRefresh();
                    }
                }
               onload.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static void getContratante(final String id, final TaskCompletionSource<Boolean> dbSource){
        Query query = mDatabaseRef.child("Contratante");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Contratante contratante = data.getValue(Contratante.class);
                    assert contratante != null;
                    if(contratante.id.equals(id)) {
                        SnapshotContratante.setContratante(contratante);
                        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                        myFirebaseInstanceIDService.onTokenRefresh();
                    }

                }
                try {
                    dbSource.setResult(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    dbSource.setResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public static void getEventos(String genero,String preco,String cidade,String data){
        Query query=mDatabaseRef.child("Evento");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static void insertArtist(String nome, String foto, DadosArtista dadosArtista,String uid,final Runnable onLoad){
        String id = mDatabaseRef.child("Artista").push().getKey();
        final Artista artista=new Artista(id,nome,null,dadosArtista,null,uid,null);
        SnapshotArtista.setArtista(artista);
        uploadPhoto(id, foto, "artista", new Runnable() {
            @Override
            public void run() {
                Artista artista1= SnapshotArtista.getArtista();
                mDatabaseRef.child("Artista").child(artista1.id).setValue(artista1);
                MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                myFirebaseInstanceIDService.onTokenRefresh();
            }
        });
        onLoad.run();
    }
    public static void insertContratante(String nome, String foto, String uid,String telefone,String estado,
                                         String cidade,final Runnable onLoad){
        String id = mDatabaseRef.child("Contratante").push().getKey();
        final Contratante contratante=new Contratante(id,nome,foto,null,uid,telefone,estado,cidade);
        SnapshotContratante.setContratante(contratante);
        uploadPhoto(id, foto, "contratante", new Runnable() {
            @Override
            public void run() {
                Contratante contratante1= SnapshotContratante.getContratante();
                mDatabaseRef.child("Contratante").child(contratante1.id).setValue(contratante1);
                MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                myFirebaseInstanceIDService.onTokenRefresh();
            }
        });
        onLoad.run();
    }
    public static void insertEvento(String id_contratante,String nome,String foto,ArrayList<String> instrumentos,
                                    double valor,ArrayList<String> fotos){
        Query query=mDatabaseRef.child("Evento");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void uploadPhoto(String id, String foto, final String entity, final Runnable onLoaded){
        Log.d(TAG,"uploading started!");
        Uri uri=Uri.fromFile(new File(foto));
        Log.d(TAG,foto);
        StorageReference storageReference;
        if (entity.equalsIgnoreCase("artista")){
            storageReference=mStorageRef.child("foto_artista/"+id+"/foto_artista");
        }
        else{
            storageReference=mStorageRef.child("foto_contratante/"+id+"/foto_contratante");
        }
        UploadTask uploadTask=storageReference.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                assert downloadUrl != null;
                if (entity.equalsIgnoreCase("artista")){
                    SnapshotArtista.getArtista().foto=downloadUrl.toString();
                }
                else{
                    SnapshotContratante.getContratante().foto=downloadUrl.toString();
                }

                onLoaded.run();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"upload falhou!");
            }
        });
    }
    public static void uploadPhotos(String id,String foto, String entity, final Runnable onLoaded){
        Log.d(TAG,"uploading started!");
        Uri uri=Uri.fromFile(new File(foto));
        Log.d(TAG,foto);
        StorageReference storageReference;
        if (entity.equalsIgnoreCase("artista")){
            storageReference=mStorageRef.child("foto_artista/"+id+"/foto_artista");
        }
        else{
            storageReference=mStorageRef.child("foto_contratante"+id+"foto_contratante");
        }
        UploadTask uploadTask=storageReference.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                assert downloadUrl != null;
                SnapshotArtista.getArtista().foto=downloadUrl.toString();
                onLoaded.run();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"upload falhou!");
            }
        });
    }

    public static FirebaseAuth getmAuth(){
        return mAuth;
    }
    public static void writeToken(String id, String token){
        mDatabaseRef.child("Artista").child(id).child("token").setValue(token);
    }
    public static void recoverFromUserUid(final String uid, final String tipo , final Runnable onLoaded){
        Query query = mDatabaseRef.child(tipo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if(Objects.equals(childDataSnapshot.child("uid").getValue(), uid)){
                        String id = (String) childDataSnapshot.child("id").getValue();
                        if (tipo.equalsIgnoreCase("Artista")){
                            SnapshotArtista.setId_artista(id);
                            Log.d("Id","Artista "+id);
                        }
                        else {
                            SnapshotContratante.setId_contratante(id);
                            Log.d("Id","Contratante "+id);
                        }
                        onLoaded.run();
                    }
                }
                onLoaded.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static void logout(){
        mAuth.signOut();
    }

}

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
    private static FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    private static DatabaseReference mDatabaseRef=mDatabase.getReference();
    private static FirebaseStorage mStorage=FirebaseStorage.getInstance();
    private static StorageReference mStorageRef=mStorage.getReference();
    private static String TAG="Firebase";

    @IgnoreExtraProperties
    public static class Artista{
        public String id;
        String nome;
        String foto;
        DadosArtista dadosArtista;
        String token;
        String uid;
        String youtube_channel;

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
        String telefone;
        String cidade;
        String estado;

        public DadosArtista(  String telefone,String cidade,String estado){
            this.telefone=telefone;
            this.cidade=cidade;
            this.estado=estado;
        }
    }

    @IgnoreExtraProperties
    public static class Contratante{
        public String id;
        String nome;
        String foto;
        String token;
        String uid;
        String telefone;

        public Contratante(){}
        public Contratante(String id,String nome,String foto,String token,
                       String uid,String telefone){
            this.id=id;
            this.nome=nome;
            this.foto=foto;
            this.token=token;
            this.uid=uid;
            this.telefone=telefone;
        }


    }

    @IgnoreExtraProperties
    public static class Evento{
        String id;
        String id_contratante;
        String nome;
        String descricao;
        String instrumentos;
        double valor;
        ArrayList<String>fotos;

        public Evento() {
        }
        public Evento(String id, String id_contratante, String nome,
                      String descricao, String instrumentos, double valor, ArrayList<String> fotos) {
            this.id = id;
            this.id_contratante = id_contratante;
            this.nome = nome;
            this.descricao = descricao;
            this.instrumentos = instrumentos;
            this.valor = valor;
            this.fotos = fotos;
        }

    }


    public static void recover_artista(String artista_id, String path, final Runnable runnable){
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
    public static void uploadPhoto(String id,String foto, String entity, final Runnable onLoaded){
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
                    if(Objects.equals(childDataSnapshot.child("user_uid").getValue(), uid)){
                        String id = (String) childDataSnapshot.child("id").getValue();
                        if (tipo.equals("Artista")){
                            SnapshotArtista.setId_artista(id);
                        }
                        else {
                            SnapshotContratante.setId_contratante(id);
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
}

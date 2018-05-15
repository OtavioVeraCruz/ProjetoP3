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
        ArrayList<String>contatos;
        String cidade;
        String estado;
        String data_nasc;

        public DadosArtista(ArrayList<String>contatos,String cidade,String estado,String data_nasc){
            this.contatos=contatos;
            this.cidade=cidade;
            this.estado=estado;
            this.data_nasc=data_nasc;
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
                if (Snapshot.getArtista()!=null){
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
                        Snapshot.getInstance().setArtista(artista);
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
    public void addArtist(String nome, String foto, DadosArtista dadosArtista,String uid){
        String id = mDatabaseRef.child("Artista").push().getKey();
        final Artista artista=new Artista(id,nome,null,dadosArtista,null,uid,null);
        Snapshot.setArtista(artista);
        uploadPhoto(id, foto, "artista", new Runnable() {
            @Override
            public void run() {
                Artista artista1=Snapshot.getArtista();
                mDatabaseRef.child("Artista").child(artista1.id).setValue(artista1);
                MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                myFirebaseInstanceIDService.onTokenRefresh();
            }
        });
    }
    public void uploadPhoto(String id,String foto, String entity, final Runnable onLoaded){
        Uri uri=Uri.fromFile(new File(foto));
        StorageReference storageReference;
        if (entity.equalsIgnoreCase("artista")){
            storageReference=mStorageRef.child("foto_artista"+id+"foto_artista");
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
                Snapshot.getArtista().foto=downloadUrl.toString();
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
    public static void recoverFromUserUid(final String uid, final Runnable onLoaded){
        Query query = mDatabaseRef.child("Artista");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if(Objects.equals(childDataSnapshot.child("user_uid").getValue(), uid)){
                        String artista_id = (String) childDataSnapshot.child("id").getValue();
                        Snapshot.setId_artista(artista_id);
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

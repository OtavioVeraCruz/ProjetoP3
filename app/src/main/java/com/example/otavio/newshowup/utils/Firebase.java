package com.example.otavio.newshowup.utils;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.otavio.newshowup.services.MyFirebaseInstanceIDService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Firebase {

    private static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    public static DatabaseReference mDatabaseRef=mDatabase.getReference();
    private static FirebaseStorage mStorage=FirebaseStorage.getInstance();
    private static StorageReference mStorageRef=mStorage.getReference();
    private static String TAG="Firebase";
    private static boolean isEventRemoved;

    @IgnoreExtraProperties
    public static class Artista implements Serializable{
        public String id;
        public String nome;
        public String foto;
        public DadosArtista dadosArtista;
        public String token;
        public String uid;
        public String youtube_channel;
        public ArrayList<String>candidaturas;

        public Artista(){}
        public Artista(String id,String nome,String foto,DadosArtista dadosArtista,String token,
                       String uid,String youtube_channel,ArrayList<String>candidaturas){
            this.id=id;
            this.nome=nome;
            this.foto=foto;
            this.dadosArtista=dadosArtista;
            this.token=token;
            this.uid=uid;
            this.youtube_channel=youtube_channel;
            this.candidaturas=candidaturas;
        }

    }
    @IgnoreExtraProperties
    public static class DadosArtista implements Serializable{
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
    public static class Evento implements Serializable{
        public String id;
        public String id_contratante;
        public String nome;
        public String descricao;
        public ArrayList<String> instrumentos;
        public String faixa_preco;
        public ArrayList<String> fotos;
        public String cidade;
        public String data;
        public ArrayList<String>candidatos;

        public Evento() {
        }
        public Evento(String id, String id_contratante, String nome,String descricao,ArrayList<String> instrumentos,
                      String faixa_preco,ArrayList<String> fotos,String cidade,String data,ArrayList<String>candidatos) {
            this.id = id;
            this.id_contratante = id_contratante;
            this.nome = nome;
            this.descricao = descricao;
            this.instrumentos = instrumentos;
            this.faixa_preco = faixa_preco;
            this.fotos = fotos;
            this.cidade=cidade;
            this.data=data;
            this.candidatos=candidatos;

        }

        public void setFotos(ArrayList<String> fotos){
            this.fotos=fotos;
        }

    }

    @IgnoreExtraProperties
    public static class Candidatura{
        public String id;
        public String id_artista;
        public String id_evento;
        public Candidatura(){}
        public Candidatura(String id,String id_artista,String id_evento){
            this.id=id;
            this.id_artista=id_artista;
            this.id_evento=id_evento;
        }
    }

    @IgnoreExtraProperties
    public static class Notifica{
        public String id;
        public String id_artista;
        public String id_contratante;
        public Notifica(){}
        public Notifica(String id,String id_artista,String id_contratante){
            this.id=id;
            this.id_artista=id_artista;
            this.id_contratante=id_contratante;
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
                    getCandidaturas(SnapshotArtista.getArtista().id,runnable);
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
    private static void getArtista(final String id, final TaskCompletionSource<Boolean> dbSource){
        Query query = mDatabaseRef.child("Artista");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
    public static void updateArtista(final Artista artista, final Runnable runnable){

        uploadPhoto(artista.id, artista.foto, "Artista", new Runnable() {
            @Override
            public void run() {
                artista.foto=SnapshotArtista.getArtista().foto;
                mDatabaseRef.child("Artista").child(artista.id).
                        setValue(artista).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            runnable.run();
                        }
                        else {
                            runnable.run();
                        }
                    }
                });

            }
        });

    }
    public static void getArtistas(final ArrayList<String>candidatos, final Runnable runnable){
        mDatabaseRef.child("Artista").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Artista artista=data.getValue(Artista.class);
                    for (int i=0;i<candidatos.size();i++){
                        assert artista != null;
                        if (artista.id.equals(candidatos.get(i))){
                            SnapshotContratante.artistas.add(artista);
                        }
                    }

                }
                runnable.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private static void getContratante(final String id, final TaskCompletionSource<Boolean> dbSource){
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
    private static void getCandidaturas(final String id_artista, final Runnable runnable){
        Query query=mDatabaseRef.child("Candidatura");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Candidatura candidatura=data.getValue(Candidatura.class);
                    assert candidatura != null;
                    if (candidatura.id_artista.equals(id_artista)){
                        SnapshotArtista.candidaturas.put(candidatura.id,candidatura);
                        getEventos(candidatura.id_evento);
                    }
                }
                runnable.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static void candidatarEvento(String id_evento,String id_artista,final Runnable runnable){

        if(SnapshotEvento.getEvento().candidatos==null) {
            SnapshotEvento.getEvento().candidatos = new ArrayList<>();
            insertCandidatura(id_artista,id_evento);
            runnable.run();
        }

        else{
            insertCandidatura(id_artista,id_evento);
            runnable.run();
        }
    }
    private static synchronized void insertCandidatura(String id_artista, String id_evento){
        String id = mDatabaseRef.child("Candidatura").push().getKey();
        if (SnapshotArtista.candidaturas.size()>0) {
            boolean existe=false;
            for (String key:SnapshotArtista.candidaturas.keySet()){
                Candidatura aux=SnapshotArtista.candidaturas.get(key);
                if (aux.id_evento.equals(id_evento)){
                    existe=true;
                }
            }
            if (!existe) {
                Candidatura candidatura = new Candidatura(id, id_artista, id_evento);
                assert id != null;
                mDatabaseRef.child("Candidatura").child(id).setValue(candidatura);
                SnapshotArtista.candidaturas.put(id,candidatura);

                SnapshotEvento.getEvento().candidatos.add(id_artista);
                mDatabaseRef.child("Evento").child(id_evento).child("candidatos").setValue(
                        SnapshotEvento.getEvento().candidatos);

                if (SnapshotArtista.getArtista().candidaturas == null) {
                    SnapshotArtista.getArtista().candidaturas = new ArrayList<>();
                }

                SnapshotArtista.getArtista().candidaturas.add(id);
                mDatabaseRef.child("Artista").child(id_artista).child("candidaturas").setValue(
                        SnapshotArtista.getArtista().candidaturas);
            }
        }
        else {
            Candidatura candidatura = new Candidatura(id, id_artista, id_evento);
            assert id != null;
            mDatabaseRef.child("Candidatura").child(id).setValue(candidatura);
            SnapshotArtista.candidaturas.put(id,candidatura);

            SnapshotEvento.getEvento().candidatos.add(id_artista);
            mDatabaseRef.child("Evento").child(id_evento).child("candidatos").setValue(
                    SnapshotEvento.getEvento().candidatos);

            if (SnapshotArtista.getArtista().candidaturas==null){
                SnapshotArtista.getArtista().candidaturas=new ArrayList<>();
            }

            SnapshotArtista.getArtista().candidaturas.add(id);
            mDatabaseRef.child("Artista").child(id_artista).child("candidaturas").setValue(
                    SnapshotArtista.getArtista().candidaturas);
        }

    }
    public static synchronized void removeCandidatura(String id_evento, String id_artista,
                                         Runnable runnable){

        for (int i=0;i<SnapshotEvento.getEvento().candidatos.size();i++){
            String aux_artistas=SnapshotEvento.getEvento().candidatos.get(i);
            if (aux_artistas.equals(id_artista)){
                SnapshotEvento.getEvento().candidatos.remove(i);
                mDatabaseRef.child("Evento").child(id_evento).child("candidatos").setValue(
                        SnapshotEvento.getEvento().candidatos);
                break;
            }
        }
        String id_candidatura="";
        for (String candidatura:SnapshotArtista.getCandidaturas().keySet()){
            Candidatura aux=SnapshotArtista.getCandidaturas().get(candidatura);
            if (aux.id_evento.equals(id_evento)){
                SnapshotArtista.candidaturas.remove(aux.id);
                mDatabaseRef.child("Candidatura").child(aux.id).setValue(null);
                id_candidatura=aux.id;
                break;
            }
        }
        for(int j=0;j<SnapshotArtista.getArtista().candidaturas.size();j++){
            if (SnapshotArtista.getArtista().candidaturas.get(j).equals(id_candidatura)){
                SnapshotArtista.getArtista().candidaturas.remove(j);
                break;
            }
        }
        mDatabaseRef.child("Artista").child(id_artista).child("candidaturas")
                .setValue(SnapshotArtista.getArtista().candidaturas);

        runnable.run();

    }
    private static void getEventos(final String id){
        Query query=mDatabaseRef.child("Evento");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Evento evento=data.getValue(Evento.class);
                    assert evento!=null;
                    if (evento.id.equals(id)){
                        if (SnapshotArtista.eventos_candidatados.size()>0) {
                            boolean existe=false;
                            for (int i = 0; i < SnapshotArtista.eventos_candidatados.size(); i++) {
                                if (SnapshotArtista.eventos_candidatados.get(i).id.equals(id)){
                                    existe=true;
                                }
                            }
                            if (!existe){
                                SnapshotArtista.eventos_candidatados.add(evento);
                            }
                        }
                        else{
                            SnapshotArtista.eventos_candidatados.add(evento);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Evento evento=dataSnapshot.getValue(Evento.class);
                    for (int i=0;i<SnapshotArtista.eventos_candidatados.size();i++){
                        assert evento != null;
                        if (evento.id.equals(SnapshotArtista.eventos_candidatados.get(i).id)){
                            SnapshotArtista.eventos_candidatados.remove(i);
                            SnapshotArtista.eventoAdapter.setEventos(SnapshotArtista.eventos_candidatados);
                            SnapshotArtista.eventoAdapter.notifyDataSetChanged();
                        }
                    }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static void insertArtist(String nome, String foto, DadosArtista dadosArtista,String uid,
                                    final Runnable onLoad){
        String id = mDatabaseRef.child("Artista").push().getKey();
        final Artista artista=new Artista(id,nome,null,dadosArtista,null,uid,null,null);
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
    public static void updateContratante(final Contratante contratante, final Runnable runnable){

        uploadPhoto(contratante.id, contratante.foto, "Contratante", new Runnable() {
            @Override
            public void run() {
                contratante.foto=SnapshotContratante.getContratante().foto;
                mDatabaseRef.child("Contratante").child(contratante.id).
                        setValue(contratante).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            runnable.run();
                        }
                        else {
                            runnable.run();
                        }
                    }
                });

            }
        });

    }
    public static void insertEvento(String id_contratante,String nome,String descricao,ArrayList<String> instrumentos,
                                    String faixa_preco,ArrayList<String> fotos,String cidade,String data,
                                    final Runnable onLoad){

       final String id_evento = mDatabaseRef.child("Evento").push().getKey();
       final Evento evento=new Evento(id_evento,id_contratante,nome,descricao,instrumentos,faixa_preco,
               fotos,cidade,data,null);

       SnapshotContratante.setEvento(evento);
       uploadPhotos(id_evento,fotos, new Runnable() {
           @Override
           public void run() {
                Evento evento1=SnapshotContratante.getEvento();
                mDatabaseRef.child("Evento").child(evento1.id).setValue(evento1);
           }
       });
       onLoad.run();
    }
    public static void updateEvento(final Evento evento, final Runnable onLoad){

                mDatabaseRef.child("Evento").child(evento.id).setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("Evento editado","com sucesso!");
                            onLoad.run();
                        }
                        else {
                            Log.d("Evento editado","sem sucesso!");
                            onLoad.run();
                        }
                    }
                });
    }
    public static void removeEvento(String id, final Runnable onLoad){

      if ( mDatabaseRef.child("Evento").child(id).setValue(null).isSuccessful()) {
          SnapshotContratante.setEvento(null);
          mStorageRef.child("fotos_evento").child(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  setIsEventRemoved(true);
                  onLoad.run();

              }
          });
      }
      else {
          setIsEventRemoved(false);
          onLoad.run();
      }

    }
    public static void setIsEventRemoved(boolean removed){
        isEventRemoved=removed;
    }
    public static boolean getIsEventRemoved(){
        return isEventRemoved;
    }

    //Upload imgs
    private static void uploadPhoto(String id, String foto, final String entity, final Runnable onLoaded){
        Log.d(TAG,"uploading started!");
        Uri uri=Uri.fromFile(new File(foto));
        Log.d(TAG,foto);
        final StorageReference storageReference;
        if (!foto.contains("firebasestorage")) {
            if (entity.equalsIgnoreCase("artista")) {
                storageReference = mStorageRef.child("foto_artista/" + id + "/foto_artista");
            } else {
                storageReference = mStorageRef.child("foto_contratante/" + id + "/foto_contratante");
            }
            final UploadTask uploadTask = storageReference.putFile(uri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (entity.equalsIgnoreCase("artista")) {
                            SnapshotArtista.getArtista().foto = downloadUri.toString();
                        } else {
                            SnapshotContratante.getContratante().foto = downloadUri.toString();
                        }
                        onLoaded.run();
                    } else {
                        Log.d(TAG, "upload falhou!");
                    }
                }
            });
        }
        else {
            Log.d("Img","Img permanece a mesma!");
            onLoaded.run();
        }

    }
    private static void uploadPhotos(String id, final ArrayList<String> fotos,final Runnable onLoaded){
        Log.d(TAG,"uploading started!");
        final ArrayList<String >aux=new ArrayList<>();
        for (int i=0;i<fotos.size();i++) {
                Uri uri = Uri.fromFile(new File(fotos.get(i)));
                final StorageReference storageReference;
                String nome_foto = (fotos.get(i).split("/"))[6];
                Log.d("Upload", nome_foto);
                storageReference = mStorageRef.child("fotos_evento/" + id + "/" + nome_foto);
                UploadTask uploadTask = storageReference.putFile(uri);

                final int finalI = i;
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        // Continue with the task to get the download URL
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            aux.add(downloadUri.toString());
                        }
                        if ((aux.size() - 1) == finalI) {
                            SnapshotContratante.getEvento().fotos.addAll(aux);
                            onLoaded.run();
                        }
                    }
                });
        }


    }

    public static FirebaseAuth getmAuth(){
        return mAuth;
    }
    public static void writeToken(String id, String token,String tipo){
        if (tipo.equalsIgnoreCase("Artista")) {
            mDatabaseRef.child("Artista").child(id).child("token").setValue(token);
        }
        else {
            mDatabaseRef.child("Contratante").child(id).child("token").setValue(token);
        }
    }
    public static void recoverFromUserUid(final String uid, final String tipo , final Runnable onLoaded){
        Query query = mDatabaseRef.child(tipo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if(Objects.equals(childDataSnapshot.child("uid").getValue(), uid)){
                        String id = (String) childDataSnapshot.child("id").getValue();
                        if (tipo.equalsIgnoreCase("Artista")){
                            SnapshotArtista.setId_artista(id);
                            Log.d("Id","Artista "+id+"\n"+"Snap: "+SnapshotArtista.getId_artista());
                        }
                        else {
                            SnapshotContratante.setId_contratante(id);
                            Log.d("Id","Contratante "+id +"\n"+"Snap: "+SnapshotContratante.getId_contratante());
                        }
                        //onLoaded.run();
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

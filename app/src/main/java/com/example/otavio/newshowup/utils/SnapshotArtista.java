package com.example.otavio.newshowup.utils;

import android.content.SharedPreferences;

public class SnapshotArtista {

    public static Firebase.Artista artista;
    public static String id_artista;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final SnapshotArtista snapshot = new SnapshotArtista();

    private SnapshotArtista() {}

    public static SnapshotArtista getInstance() {
        return snapshot;
    }

    public static Firebase.Artista getArtista() {
        return artista;
    }

    public static void setArtista(Firebase.Artista artista) {
        SnapshotArtista.artista = artista;
    }

    public static String getId_artista() {
        return id_artista;
    }

    public static void setId_artista(String id_artista) {
        SnapshotArtista.id_artista = id_artista;
    }

    public static void reset(){
        artista=null;
        id_artista=null;

    }
}

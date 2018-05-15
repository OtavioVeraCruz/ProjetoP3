package com.example.otavio.newshowup.utils;

import android.content.SharedPreferences;

public class Snapshot {

    public static Firebase.Artista artista;
    public static String id_artista;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final Snapshot snapshot = new Snapshot();

    private Snapshot() {}

    public static Snapshot getInstance() {
        return snapshot;
    }

    public static Firebase.Artista getArtista() {
        return artista;
    }

    public static void setArtista(Firebase.Artista artista) {
        Snapshot.artista = artista;
    }

    public static String getId_artista() {
        return id_artista;
    }

    public static void setId_artista(String id_artista) {
        Snapshot.id_artista = id_artista;
    }
}

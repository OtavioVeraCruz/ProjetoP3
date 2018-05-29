package com.example.otavio.newshowup.utils;

import android.content.SharedPreferences;

public class SnapshotContratante {
    public static Firebase.Contratante contratante;
    public static String id_contratante;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final SnapshotContratante snapshotContratante = new SnapshotContratante();

    private SnapshotContratante() {}

    public static SnapshotContratante getInstance() {
        return snapshotContratante;
    }

    public static Firebase.Contratante getContratante() {
        return contratante;
    }

    public static void setContratante(Firebase.Contratante contratante) {
        SnapshotContratante.contratante = contratante;
    }

    public static String getId_contratante() {
        return id_contratante;
    }

    public static void setId_contratante(String id_artista) {
        SnapshotArtista.id_artista = id_artista;
    }

    public static void reset(){
        contratante=null;
        id_contratante=null;
    }

}

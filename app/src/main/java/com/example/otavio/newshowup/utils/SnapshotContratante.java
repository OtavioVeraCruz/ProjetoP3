package com.example.otavio.newshowup.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class SnapshotContratante {
    public static Firebase.Contratante contratante;
    public static String id_contratante;
    public static ArrayList<Firebase.Artista> artistas=new ArrayList<>();

    public static Firebase.Evento getEvento() {
        return evento;
    }

    public static void setEvento(Firebase.Evento evento) {
        SnapshotContratante.evento = evento;
    }

    public static Firebase.Evento evento;
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

    public static void setId_contratante(String id_contratante) {
        SnapshotContratante.id_contratante = id_contratante;
    }

    public static void reset(){
        contratante=null;
        id_contratante=null;
        evento=null;
    }

}

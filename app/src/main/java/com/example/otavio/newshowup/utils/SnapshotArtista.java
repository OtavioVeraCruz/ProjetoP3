package com.example.otavio.newshowup.utils;

import com.example.otavio.newshowup.artista.EventoAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SnapshotArtista {

    public static Firebase.Artista artista;
    public static String id_artista;
    private static final SnapshotArtista snapshot = new SnapshotArtista();
    public static HashMap<String,Firebase.Candidatura> candidaturas=new HashMap<>();
    public static ArrayList<Firebase.Evento>eventos=new ArrayList<>();
    public static ArrayList<Firebase.Evento>eventos_candidatados=new ArrayList<>();
    public static EventoAdapter eventoAdapter=null;

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

    public static HashMap<String,Firebase.Candidatura> getCandidaturas() {
        return candidaturas;
    }

    public static void setEventoAdapter(EventoAdapter adapter){
        eventoAdapter=adapter;
    }

    public static void reset(){
        artista=null;
        id_artista=null;
        candidaturas=null;
        eventos_candidatados=null;
        eventos_candidatados=null;
    }
}

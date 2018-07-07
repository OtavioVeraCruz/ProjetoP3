package com.example.otavio.newshowup.utils;

public class SnapshotEvento {
   public static String id_evento;
    public static Firebase.Evento evento;

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        SnapshotEvento.id_evento = id_evento;
    }
    private SnapshotEvento(){}
    public static Firebase.Evento getEvento() {
        return evento;
    }

    public static void setEvento(Firebase.Evento evento) {
        SnapshotEvento.evento = evento;
    }

    public static void reset(){
        id_evento=null;
        evento=null;
    }
}

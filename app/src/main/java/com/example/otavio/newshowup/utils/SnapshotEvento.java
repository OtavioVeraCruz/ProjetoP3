package com.example.otavio.newshowup.utils;

public class SnapshotEvento {
    String id_evento;
    Firebase.Evento evento;

    public Firebase.Evento getEvento() {
        return evento;
    }

    public void setEvento(Firebase.Evento evento) {
        this.evento = evento;
    }
}

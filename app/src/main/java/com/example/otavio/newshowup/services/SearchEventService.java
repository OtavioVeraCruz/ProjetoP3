package com.example.otavio.newshowup.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SearchEventService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SearchEventService(String name) {
        super(name);
    }

    public SearchEventService() {
        super("SearchEventService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String[]query=intent.getStringExtra("query").split("/");
        String genero=query[0];
        String preco=query[1];
        String cidade=query[2];
        String data=query[3];




    }
}

package com.example.otavio.newshowup.services;

import android.util.Log;

import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotArtista;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (SnapshotArtista.getArtista()!=null) {
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            // TODO: Implement this method to send any registration to your app's servers.
            if (refreshedToken != null) {
                sendRegistrationToServer(refreshedToken,"Artista");
            }
        }
        else if (SnapshotContratante.getContratante()!=null){
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            // TODO: Implement this method to send any registration to your app's servers.
            if (refreshedToken != null) {
                sendRegistrationToServer(refreshedToken,"Contratante");
            }
        }
    }
    /**
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token,String tipo) {// Add custom implementation, as needed.
        if (tipo.equalsIgnoreCase("Artista")) {
            Firebase.writeToken(SnapshotArtista.getArtista().id, token);
        }
        else{
            Firebase.writeToken(SnapshotContratante.getContratante().id, token);
        }
    }

}

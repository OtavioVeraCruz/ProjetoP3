package com.example.otavio.newshowup.services;

import android.util.Log;

import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.Snapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (Snapshot.getArtista()!=null) {
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            // TODO: Implement this method to send any registration to your app's servers.
            if (refreshedToken != null) {
                sendRegistrationToServer(refreshedToken);
            }
        }
    }
    /**
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {// Add custom implementation, as needed.
        Firebase.writeToken(Snapshot.getArtista().id, token);
    }

}

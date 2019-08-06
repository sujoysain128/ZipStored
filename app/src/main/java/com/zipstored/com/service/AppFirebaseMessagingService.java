package com.zipstored.com.service;

import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by root on 16/1/18.
 */

public class AppFirebaseMessagingService extends FirebaseMessagingService {
    final static String GROUP_KEY_EMAILS = "app.foodanytime.android.service.interest";
    public static final String PRIMARY_CHANNEL = "chat";
    public static final String SECONDARY_CHANNEL = "interest";
    public static final String TERTIORY_CHANNEL = "admin";
    private static String str;


    @Override
    public void onNewToken(String s) {
        System.out.println("remoteMessage new token ====> "+s);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        super.onNewToken(s);


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("remoteMessage ====> "+remoteMessage);
        //super.onMessageReceived(remoteMessage);

        /*if (AuthenticationManager.getmAuth().getCurrentUser() != null) {
            sendNotification(body, title, notificationType);
        }*/

    }



}

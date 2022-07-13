package com.example.connect.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.connect.R;
import com.example.connect.ui.IncomingCallActivity;
import com.example.connect.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);

        if (type != null) {
            if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                Intent intent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_TYPE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE)
                );
                intent.putExtra(
                        "userId",
                        remoteMessage.getData().get("userId")
                );
                intent.putExtra(
                        "userName",
                        remoteMessage.getData().get("userName")
                );
                intent.putExtra(
                        "userImage",
                        remoteMessage.getData().get("userImage")
                );
                intent.putExtra(
                        "userToken",
                        remoteMessage.getData().get(Constants.REMOTE_MSG_INVITER_TOKEN)
                );
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_ROOM,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM)
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)) {
                Intent intent = new Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE);
                intent.putExtra(
                        Constants.REMOTE_MSG_INVITATION_RESPONSE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_INVITATION_RESPONSE)
                );
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        } else {
            String userId, userName, userToken;
            userId = remoteMessage.getData().get("userId");
            userName = remoteMessage.getData().get("userName");
            userToken = remoteMessage.getData().get("userToken");

            int notificationId = new Random().nextInt();
            String channelId = "chat_message";

            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(userId, "userId");
            intent.putExtra(userName, "userName");
            intent.putExtra(userToken, "userToken");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
            builder.setContentTitle(userName);
            builder.setContentText(remoteMessage.getData().get("message"));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                    remoteMessage.getData().get("message"))
            );
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence channelName = "Chat Message";
                String channelDescription = "This notification channel is used for chat message notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                channel.setDescription(channelDescription);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(notificationId, builder.build());
        }
    }
}

package ca.dal.csci3130.quickcash.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Using FirebaseMessagingService which needs this method to observer token changes
     *
     * @param token
     */
    @Override
    public void onNewToken(@NonNull String token) { //NOSONAR
        // Sonar excluded - reason: need this method to fulfill requirements of the notification service.
        super.onNewToken(token);
    }

    /**
     * Sends notification to people who have matching preferences
     *
     * @param message
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        // If the notification message received is null, return.
        if (message.getNotification() == null) {
            return;
        }

        // Extract fields from the notification message.
        final String title = message.getNotification().getTitle();
        final String body = message.getNotification().getBody();

        final Map<String, String> data = message.getData();
        final String jobId = data.get("jobId");
        final String jobLocation = data.get("jobLocation");

        // Create an intent to start activity when the notification is clicked.
        Intent intent = new Intent(this, ViewPushNotificationActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("jobId", jobId);
        intent.putExtra("jobLocation", jobLocation);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        // Create a notification that will be displayed in the notification tray.
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "JOBS")
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(com.google.firebase.messaging.R.drawable.gcm_icon);

        // Add the intent to the notification.
        notificationBuilder.setContentIntent(pendingIntent);

        // Notification manager to display the notification.
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();

        // If the build version is greater than, put the notification in a channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("JOBS", "JOBS", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Display the push notification.
        notificationManager.notify(id, notificationBuilder.build());
    }
}
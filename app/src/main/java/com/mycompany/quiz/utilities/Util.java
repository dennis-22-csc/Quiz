package com.mycompany.quiz.utilities;

import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.service.notification.StatusBarNotification;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mycompany.quiz.R;
import com.mycompany.quiz.quiz.QuizActivity;
import com.mycompany.quiz.quiz.QuizReceiver;

import java.io.IOException;
import java.time.Duration;


public class Util {
    Context context;

    public Util(Context context) {
        this.context=context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void showQuizNotification() {
        //Variable for ID of notification channel
        String channel_id = "notification_channel";

        //Create Notification Channel
        //Creates a NotificationChannel object
        NotificationChannel notificationChannel = new NotificationChannel(channel_id, "quiz_app",
                NotificationManager.IMPORTANCE_HIGH);

        //Configures the notification channel, by calling relevant methods
        notificationChannel.setDescription("no sound");
        notificationChannel.setSound(null, null);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);

        // Creates a NotificationManager object
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Calls the createNotificationChannel method belonging to the NotificationManager object
        manager.createNotificationChannel(notificationChannel);

        Intent showIntent = new Intent(context, QuizActivity.class);
        PendingIntent showQuizPendingIntent = PendingIntent.getActivity(context,
                0, showIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Intent rescheduleIntent = new Intent(context, QuizReactivator.class);
        rescheduleIntent.putExtra("action", "reschedule");
        PendingIntent rescheduleQuizPendingIntent = PendingIntent.getBroadcast(context,
                0, rescheduleIntent,PendingIntent.FLAG_CANCEL_CURRENT);


        //Build Notification
        //Creates a Builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id);

        //Configures the notification by calling methods belonging to the Builder object
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentText("Question has arrived.");
        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        //builder.setContentTitle("Quiz Question");
        //builder.setPriority(NotificationCompat.PRIORITY_LOW);
        //builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        //builder.setAutoCancel(true);
        builder.setOngoing(true);
        //builder.setWhen(0);
        builder.addAction(0, "Show", showQuizPendingIntent);
        builder.addAction(0,"Reschedule", rescheduleQuizPendingIntent);


        //Display Notification
        //Creates a NotificationManagerCompat object
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //Calls the notify method belonging to the notificationManagerCompat object
        notificationManager.notify(0, builder.build());

    }

    //Method to cancel Notification
    public void cancelNotification(int id) {
            NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(id);
    }



    public int checkBatteryLevel() {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(context.BATTERY_SERVICE);
        int batLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return batLevel;
    }

    public boolean isInteractive() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = powerManager.isInteractive();
        return isScreenOn;
    }


    public boolean isNotificationVisible() {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == 0) {
                return true;
            }
        }
        return false;
    }


    public void soundAudio() throws IOException {
            AssetFileDescriptor afd =afd =context.getAssets().openFd("oga.aac");
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
    }


    public static int[] getRangeArray(int start, int end){
        int[] rangeArray = new int[end-start + 1];

        for(int i = 0; i < rangeArray.length; i++){
            rangeArray[i] = start + i;
        }
        return rangeArray; // It includes start and end values as well, pls adjust according to your need
    }

    public void showPauseNotification() {
        //Variable for ID of notification channel
        String channel_id = "notification_channel";

        //Create Notification Channel
        //Creates a NotificationChannel object
        NotificationChannel notificationChannel = new NotificationChannel(channel_id, "quiz_app",
                NotificationManager.IMPORTANCE_HIGH);

        //Configures the notification channel, by calling relevant methods
        notificationChannel.setDescription("no sound");
        notificationChannel.setSound(null, null);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);

        // Creates a NotificationManager object
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Calls the createNotificationChannel method belonging to the NotificationManager object
        manager.createNotificationChannel(notificationChannel);

        Intent intent=new Intent(context, QuizReactivator.class);
        intent.putExtra("action", "resume");
        PendingIntent resumeQuizPendingIntent = PendingIntent.getBroadcast(context,
                1, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        //Intent pauseQuizIntent=new Intent(context, QuizReactivator.class);
        intent.putExtra("action", "pause");
        PendingIntent pauseQuizPendingIntent = PendingIntent.getBroadcast(context,
                2, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        //Build Notification
        //Creates a Builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id);

        //Configures the notification by calling methods belonging to the Builder object
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentText("Like to resume paused quiz");
        builder.setAutoCancel(true);
        builder.addAction(0, "Yes", resumeQuizPendingIntent);
        builder.addAction(0,"No", pauseQuizPendingIntent);


        //Display Notification
        //Creates a NotificationManagerCompat object
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //Calls the notify method belonging to the notificationManagerCompat object
        notificationManager.notify(201, builder.build());

    }

    public String getTime(long milliseconds){
        Duration duration = Duration.ofMillis(milliseconds);

        long seconds = duration.getSeconds();

        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;

        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}


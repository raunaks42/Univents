package com.lassi.univents;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class NotifHandler extends Service {

    private Looper serviceLooper;
    String e_date;
    String eventName;
    private ServiceHandler serviceHandler;
    int notifID = new Random().nextInt(6969) + 10;
    int broadcastID = new Random().nextInt(6969) + 10;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                Date e_date_d = sdf.parse(e_date);
                Date currDate = Calendar.getInstance().getTime();
                Date alertDate = new Date(e_date_d.getTime() - 7200000);
                long millsec = Math.abs(alertDate.getTime() - currDate.getTime());  //time till 2 hours before event

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "idk")
                        .setSmallIcon(android.R.drawable.presence_away)
                        .setContentTitle(eventName)
                        .setContentText("The event starts in two hours")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                createNotificationChannel();    //builds notif

                Intent notificationIntent = new Intent(getApplicationContext(), NotificationPublisher.class);
                notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notifID);
                notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), broadcastID, notificationIntent, 0); //pending intent to broadcast reciever that will create notif

                long futureInMillis = SystemClock.elapsedRealtime() + millsec;
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);  //at set time, pendingintent activated
            }
            catch (ParseException e) {

            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "You shall be notified 2 hours prior.", Toast.LENGTH_SHORT).show();

        e_date=intent.getStringExtra("edate");
        eventName=intent.getStringExtra("ename");

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_REDELIVER_INTENT;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "You have been notified.", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.idk);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("idk", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

package mystudylife.pixedar.com.mystudylife;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;


public class MyService extends Service {

    BroadcastReceiver mReceiver = null;
    ArrayList<String> currentDay;
    Alarm alarm = new Alarm();
    Schedule schedule;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    static boolean checkLuckyNumber = true;

    @Override
    public void onCreate() {
        schedule = new Schedule();
        currentDay = schedule.getScheduleForCurrentDay();
        schedule.clear();

        GetReplacements getReplacements = new GetReplacements(this, builder, notificationManager);
        getReplacements.execute();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.lesson);
        builder = new NotificationCompat.Builder(this);
        builder.setLargeIcon(bm)
                .setSmallIcon(R.drawable.small_icon_24dp)
                .setColor(Color.rgb(0, 153, 204))
                .setPriority(1)
                .setAutoCancel(false);
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pi);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        mReceiver = new ScreenReceiver(notificationManager, builder, schedule, currentDay, this);
        registerReceiver(mReceiver, filter);
        alarm.setAlarm(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
        // START YOUR TASKS
        // return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        notificationManager.cancelAll();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

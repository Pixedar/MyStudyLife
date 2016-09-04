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
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyService extends Service {

    BroadcastReceiver mReceiver = null;
    ArrayList<String> currentDay;
    Alarm alarm = new Alarm();
    Schedule schedule;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    public static ArrayList<ReplacementDetails> replacements = new ArrayList<>();
    public static getReplacements getReplacements;
    boolean checkLuckyNumber = true;

    @Override
    public void onCreate() {
        schedule = new Schedule();
        currentDay = schedule.getScheduleForCurrentDay();
        schedule.clear();

        getReplacements = new getReplacements();
        getReplacements.execute();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.Lesson);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setLargeIcon(bm)
                .setSmallIcon(R.drawable.Small_icon)
                .setColor(Color.rgb(0, 183 - 40, 220 - 40))
                .setPriority(1)
                .setAutoCancel(false);
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        mReceiver = new ScreenReceiver(mNotificationManager, mBuilder, schedule, currentDay);
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
        mNotificationManager.cancelAll();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class getReplacements extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... unused) {
            try {
                ArrayList<TeacherReplacement> missingTeachers = SiteParser.getReplacements();
                for (TeacherReplacement missingTeacher : missingTeachers) {
                    for (ReplacementDetails details : missingTeacher.replacements) {
                        if (details.description.contains("3 aLO")) {
                            replacements.add(details);
                            if (details.description.contains("Uczniowie przychodzą później")) {
                                Bitmap a = BitmapFactory.decodeResource(getResources(), R.mipmap.Present);
                                mBuilder.setLargeIcon(a);
                                mBuilder.setAutoCancel(true);
                                mBuilder.setContentTitle(details.description);
                                mNotificationManager.notify(1, mBuilder.build());
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.Lesson);
                                mBuilder.setLargeIcon(b);
                                mBuilder.setAutoCancel(false);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (checkLuckyNumber) {
                try {
                    LuckyNumbers luckyNumbers = LuckyNumbers.getLuckyNumbers();
                    if (luckyNumbers.getA() == 35 || luckyNumbers.getB() == 35) {
                        Bitmap a = BitmapFactory.decodeResource(getResources(), R.mipmap.Present);
                        mBuilder.setLargeIcon(a);
                        mBuilder.setContentTitle("Masz sczęśliwy !!!");
                        mBuilder.setAutoCancel(true);
                        mNotificationManager.notify(3, mBuilder.build());
                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.Lesson);
                        mBuilder.setLargeIcon(b);
                        mBuilder.setAutoCancel(false);
                    }
                    checkLuckyNumber = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


    }
}

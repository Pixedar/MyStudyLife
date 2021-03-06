package mystudylife.pixedar.com.mystudylife;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;


public class ScreenReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Schedule schedule;
    private ArrayList<String> currentDay;
    private GetReplacements getReplacements;

    public ScreenReceiver(NotificationManager notificationManager, NotificationCompat.Builder builder, Schedule schedule, ArrayList<String> currentDay, Context context) {
        this.notificationManager = notificationManager;
        this.builder = builder;
        this.schedule = schedule;
        this.currentDay = currentDay;
        getReplacements = new GetReplacements(context, this.builder, this.notificationManager);
        getReplacements.execute();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Calendar calendar = Calendar.getInstance();
            int index = 0;
            int time = 0;
            String lessonName = null;

            for (String lesson : currentDay) {
                if (calendar.before(schedule.getDuration(Calendar.getInstance(), index))) {
                    try {
                        lessonName = "Następna: " + currentDay.get(index + 1);
                        for (ReplacementDetails details : getReplacements.getReplacements()) {
                            if (index + 1 == details.lesson - 1) {
                                lessonName = details.description.substring(details.description.indexOf("- ") + 1) + details.replacer;
                                break;
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        lessonName = "Wolne!!";
                        builder.setColor(Color.rgb(155, 204, 95));
                        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.home);
                        builder.setLargeIcon(bm);
                    }
                    long difference = schedule.getDuration(calendar, index).getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                    time = (int) difference / 60000;
                    if (time > 45) {
                        time = time - 45;
                        lessonName = lesson;
                        getReplacements = new GetReplacements(context, builder, notificationManager);
                        getReplacements.execute();
                    }

                    break;
                }
                index++;
            }

            builder.setContentTitle("Do dzownka: " + String.valueOf(time) + " min");
            builder.setContentText(lessonName);

            if (lessonName == null) {
                context.stopService(new Intent(context, MyService.class));
            } else {
                notificationManager.notify(0, builder.build());
            }

        }

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            notificationManager.cancel(0);
        }
    }


}

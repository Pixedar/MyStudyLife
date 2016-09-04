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

    public ScreenReceiver(NotificationManager notificationManager, NotificationCompat.Builder builder, Schedule schedule, ArrayList<String> currentDay) {
        this.notificationManager = notificationManager;
        this.builder = builder;
        this.schedule = schedule;
        this.currentDay = currentDay;
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
                    long difference = schedule.getDuration(calendar, index).getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                    time = (int) difference / 60000;
                    if (time > 45) {
                        time = time - 45;
                    }
                    try {
                        lessonName = currentDay.get(index + 1);
                        for (ReplacementDetails details : MyService.replacements) {
                            if (index + 1 == details.lesson - 1) {
                                lessonName = details.description.substring(details.description.indexOf("- ") + 1) + details.replacer;
                                break;
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        lessonName = "Wolne!!";
                        builder.setColor(Color.GREEN);
                        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.Home);
                        builder.setLargeIcon(bm);
                    }
                    break;
                }
                index++;
            }

            builder.setContentTitle("Do dzownka: " + String.valueOf(time) + " min");
            builder.setContentText("Następna: " + lessonName);

            if (lessonName == null) {
                if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 8) {
                    context.stopService(new Intent(context, MyService.class));
                } else {
                    MyService.getReplacements.execute();
                    builder.setContentText("");
                    int i = 0;
                    for (String lesson : currentDay) {
                        i++;
                        if (lesson != null) {
                            builder.setContentText(lesson);
                            for (ReplacementDetails details : MyService.replacements) {
                                if (i == details.lesson) {
                                    builder.setContentTitle(details.description);
                                    break;
                                }
                            }
                            notificationManager.notify(0, builder.build());
                            break;
                        }
                    }
                }
            } else {
                notificationManager.notify(0, builder.build());
            }

        }

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            notificationManager.cancel(0);
        }
    }

}

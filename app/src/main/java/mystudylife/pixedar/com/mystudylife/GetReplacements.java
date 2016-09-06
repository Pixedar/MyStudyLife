package mystudylife.pixedar.com.mystudylife;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GetReplacements extends AsyncTask<Void, Void, List<String>> {
    private Context context;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    ArrayList<ReplacementDetails> replacements = new ArrayList<>();

    public GetReplacements(Context context, NotificationCompat.Builder builder, NotificationManager notificationManager) {
        this.context = context;
        this.builder = builder;
        this.notificationManager = notificationManager;
    }

    @Override
    protected List<String> doInBackground(Void... unused) {

        try {
            ArrayList<TeacherReplacement> missingTeachers = SiteParser.getReplacements();
            for (TeacherReplacement missingTeacher : missingTeachers) {
                for (ReplacementDetails details : missingTeacher.replacements) {
                    if (details.description.contains("3 aLO")) {
                        replacements.add(details);
                        if (details.lesson == 1) {
                            Bitmap a = BitmapFactory.decodeResource(context.getResources(), R.mipmap.present);
                            builder.setLargeIcon(a);
                            builder.setAutoCancel(true);
                            builder.setContentTitle(details.description);
                            builder.setColor(Color.RED);
                            notificationManager.notify(1, builder.build());
                            Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.lesson);
                            builder.setLargeIcon(b);
                            builder.setAutoCancel(false);
                            builder.setColor(Color.rgb(0, 153, 204));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MyService.checkLuckyNumber) {
            try {
                LuckyNumbers luckyNumbers = LuckyNumbers.getLuckyNumbers();
                if (luckyNumbers.getA() == 35 || luckyNumbers.getB() == 35) {
                    Bitmap a = BitmapFactory.decodeResource(context.getResources(), R.mipmap.present);
                    builder.setLargeIcon(a);
                    builder.setColor(Color.RED);
                    builder.setContentTitle("Masz sczęśliwy !!!");
                    builder.setAutoCancel(true);
                    notificationManager.notify(3, builder.build());
                    Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.lesson);
                    builder.setLargeIcon(b);
                    builder.setAutoCancel(false);
                    builder.setColor(Color.rgb(0, 153, 204));
                }
                MyService.checkLuckyNumber = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public ArrayList<ReplacementDetails> getReplacements() {
        return replacements;
    }
}


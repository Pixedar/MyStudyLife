package mystudylife.pixedar.com.mystudylife;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wiktor on 2016-09-02.
 */
public class Schedule {
    private ArrayList<String> d1 = new ArrayList<>();
    private ArrayList<String> d2 = new ArrayList<>();
    private ArrayList<String> d3 = new ArrayList<>();
    private ArrayList<String> d4 = new ArrayList<>();
    private ArrayList<String> d5 = new ArrayList<>();


    public Schedule() {
        d1.add("CAD MA 032");
        d1.add("j.niemiecki Sz 206");
        d1.add("WF SM sg5");
        d1.add("WF SM sg4");
        d1.add("matematyka WM 201");
        d1.add("j.polski PM 203");
        d1.add("religia MG w2");
        d1.add("inf CW 100");
        d1.add("inf CW 100");

        d2.add("E");
        d2.add("E");
        d2.add("j.angielski ST 304");
        d2.add("j.angielski ST 304");
        d2.add("j.polski PM 203");
        d2.add("religia MG w2");
        d2.add("matematyka WM 201");
        d2.add("fizyka KG 307");
        d2.add("HIS B_ 202");

        d3.add("E");
        d3.add("E");
        d3.add("matematyka WM 201");
        d3.add("fizyka KG 307");
        d3.add("j.polski PM 203");
        d3.add("matematyka WM 201");

        d4.add("E");
        d4.add("j.angielski ST 304");
        d4.add("j.niemiecki Sz 205");
        d4.add("matematyka WM 201");
        d4.add("fizyka KG 307");
        d4.add("j.polski PM 203");
        d4.add("g.wych CW 119");

        d5.add("WF SM sg2");
        d5.add("fizyka KG 307");
        d5.add("HIS B_ 300");
        d5.add("matematyka WM 201");
    }

    public ArrayList<String> getScheduleForCurrentDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
            case Calendar.MONDAY:
                return d1;
            case Calendar.TUESDAY:
                return d2;
            case Calendar.WEDNESDAY:
                return d3;
            case Calendar.THURSDAY:
                return d4;
            case Calendar.FRIDAY:
                return d5;
        }
        return d1;
    }

    public void clear() {
        d1 = null;
        d2 = null;
        d3 = null;
        d4 = null;
        d5 = null;
    }

    public Calendar getDuration(Calendar calendar, int index) {
        switch (index) {
            case 0:
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 45);
                return calendar;
            case 1:
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 35);
                return calendar;
            case 2:
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 25);
                return calendar;
            case 3:
                calendar.set(Calendar.HOUR_OF_DAY, 11);
                calendar.set(Calendar.MINUTE, 25);
                return calendar;
            case 4:
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 15);
                return calendar;
            case 5:
                calendar.set(Calendar.HOUR_OF_DAY, 13);
                calendar.set(Calendar.MINUTE, 5);
                return calendar;
            case 6:
                calendar.set(Calendar.HOUR_OF_DAY, 13);
                calendar.set(Calendar.MINUTE, 55);

                return calendar;
            case 7:
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 45);
                return calendar;
            case 8:
                calendar.set(Calendar.HOUR_OF_DAY, 15);
                calendar.set(Calendar.MINUTE, 35);
                return calendar;
        }
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        return calendar;
    }

}


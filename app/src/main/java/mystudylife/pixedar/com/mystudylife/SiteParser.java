package mystudylife.pixedar.com.mystudylife;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SiteParser {
    private static final String TEACHER_REPLACEMENT_SIZE = "http://zastepstwa.lukasiewicz.gorlice.pl/";


    public static ArrayList<TeacherReplacement> getReplacements() throws IOException {
        ArrayList<TeacherReplacement> replacements = new ArrayList<TeacherReplacement>();

        Document doc = Jsoup.connect(TEACHER_REPLACEMENT_SIZE).get();
        Elements tableTr = doc.select("tr");

        String masterDateText = doc.select("nobr").get(0).ownText();
        masterDateText = masterDateText.substring("Zastępstwa w dniu".length());
        masterDateText = masterDateText.split(" ")[0];

        readTeachers(replacements, tableTr, masterDateText, 1);

        return replacements;
    }

    private static void readTeachers(ArrayList<TeacherReplacement> replacements, Elements tableTr, String masterDateText, int index) {
        Element teacherInfo = tableTr.get(index);
        int endIndex = findLastIndex(tableTr, index);

        String replacementHeader = teacherInfo.select("td").get(0).ownText();
        String[] header = replacementHeader.split(" / ");
        String name = header[0];
        String date;

        if (header.length > 1)
            date = header[1].split(" ")[0];
        else
            date = masterDateText;

        TeacherReplacement teacherReplacement = new TeacherReplacement(name, date);
        replacements.add(teacherReplacement);

        for (int i = index + 2; i < endIndex; i++) {
            Elements repInfo = tableTr.get(i).select("td");

            ReplacementDetails details = new ReplacementDetails();

            details.lesson = Integer.valueOf(repInfo.get(0).ownText());
            details.description = repInfo.get(1).ownText();
            details.replacer = repInfo.get(2).ownText();
            details.warnings = repInfo.get(3).ownText();

            details.description = details.description.replaceAll(String.valueOf((char) 160), " ");
            details.replacer = details.replacer.replaceAll(String.valueOf((char) 160), " ");
            details.warnings = details.warnings.replaceAll(String.valueOf((char) 160), " ");

            teacherReplacement.replacements.add(details);
        }

        if (endIndex != tableTr.size())
            readTeachers(replacements, tableTr, masterDateText, endIndex + 1);
    }

    private static int findLastIndex(Elements tableTr, int start) {
        for (int i = start; i < tableTr.size(); i++) {
            Element data = tableTr.get(i);

            String text = data.toString();
            if (countMatches(text, "&nbsp") == 5) return i;
        }

        return tableTr.size();
    }


    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}

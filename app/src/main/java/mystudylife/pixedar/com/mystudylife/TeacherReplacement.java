package mystudylife.pixedar.com.mystudylife;

import java.util.ArrayList;

public class TeacherReplacement {
    private String teacher;
    private String date;

    public ArrayList<ReplacementDetails> replacements;

    public TeacherReplacement(String teacher, String date) {
        this.date = date;
        this.teacher = teacher;

        replacements = new ArrayList<>();
    }

    public TeacherReplacement() {
    }

    public TeacherReplacement(TeacherReplacement rep) {
        this.teacher = rep.getTeacher();
        this.date = rep.getDate();
    }

    public String getDate() {
        return date;
    }

    public String getTeacher() {
        return teacher;
    }
}

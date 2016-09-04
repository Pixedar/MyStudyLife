package mystudylife.pixedar.com.mystudylife;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LuckyNumbers {
    private static final String NUMBERS_SITE = "http://lukasiewicz.gorlice.pl/";

    private String date;
    private int a;
    private int b;

    public LuckyNumbers() {
    }

    public LuckyNumbers(String date, int a, int b) {
        this.date = date;
        this.a = a;
        this.b = b;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }

    public String getDate() {
        return date;
    }

    public static LuckyNumbers getLuckyNumbers() throws IOException {
        Document doc = Jsoup.connect(NUMBERS_SITE).get();
        Elements luckyNumbersBoxDiv = doc.select("div[id=lucky_number_box]");
        Elements luckyNumberParagraph = luckyNumbersBoxDiv.select("p[class=\"number\"]");
        Elements dateElementParagraph = luckyNumbersBoxDiv.select("p[class=\"text\"]");

        String[] numbersString = luckyNumberParagraph.get(0).ownText().split(" ");

        String date = dateElementParagraph.get(0).ownText();
        int numA = Integer.valueOf(numbersString[0]);
        int numB = Integer.valueOf(numbersString[1]);

        return new LuckyNumbers(date, numA, numB);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LuckyNumbers that = (LuckyNumbers) o;

        if (a != that.a) return false;
        if (b != that.b) return false;
        if (!date.equals(that.date)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + a;
        result = 31 * result + b;
        return result;
    }
}

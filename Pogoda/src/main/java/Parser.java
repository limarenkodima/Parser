import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Document getPage() throws IOException {
       String url = "http://pogoda.msk.ru/"; // URL сайта, с которо происходит сбор информации
        Document page = Jsoup.parse(new URL(url), 3000);
        // parse формирует объект Document page и собирает страницу
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
            if (matcher.find()){
                return matcher.group();
            }
                throw new Exception("Не смог найти");
    }

    private static int printPartValues(Elements values, int index){
        int iterationCount = 4; // в каждой графе в общем случае всего 4 элемента (Утро День Вечер Ночь)
        if (index == 0) {
            Element valueLn = values.get(index);
                boolean isMorning = valueLn.text().contains("Ночь");
            //int iterationCount = 4;
                    if (isMorning) {
                        iterationCount = 5; // Частный случай, когда (Ночь Утро День Вечер Ночь)
                    }
                        for (int i = 0; i < iterationCount; i++) {
                            Element valueLine = values.get(index + i);
                                for (Element td : valueLine.select("td")) {
                                System.out.print(td.text() + "    ");
                        }
                                System.out.println();
                        }
                        return iterationCount;

        }
        else {
            for(int i = 0; i < iterationCount; i++){
                Element valueLine = values.get(index + i);
            for(Element td : valueLine.select("td")){
               System.out.print(td.text() + "    ");
            }
            System.out.println();
            }
        }
       return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        // css query language
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name: names){
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date+ "   Явления                                  Температура  Давление  Влажность  Ветер");
            int iterationCount =  printPartValues(values, index);
            index = index + iterationCount;
        }
    }
}

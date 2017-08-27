package com.pedrojtmartins.racingcalendar.eventResults;

import com.pedrojtmartins.racingcalendar.models.EventResultUnit;

import java.util.ArrayList;

import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.cleanAfter;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.cleanBefore;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.getString;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.getStringAfterValue;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.splitAndIgnorePositions;

/**
 * Pedro Martins
 * 18/08/2017
 */

public class RaceResultsManager {

    private static String placeHolder = "{*}";
    private static String getUrl(int id) {
        switch (id) {
            case 1:
                return "http://classic.autosport.com/results.php?s=0&y=2017&r=201700" + placeHolder + "&c=2";

            case 2:
                return "http://classic.autosport.com/results.php?s=202&y=2016&r=201602" + placeHolder + "&c=2";

            case 3:
                return "http://classic.autosport.com/results.php?s=901&y=2017&r=201709" + placeHolder + "&c=2";

            case 4:
                return "http://classic.autosport.com/results.php?s=630&y=2017&r=201760" + placeHolder + "&c=2";

//            case 5:
//                return "http://classic.autosport.com/results.php?s=70&y=2017&r=201770" + placeHolder + "&c=2";

            case 7:
                return "http://classic.autosport.com/results.php?s=11&y=2017&r=201711" + placeHolder + "&c=2";

            case 11:
                return "http://classic.autosport.com/results.php?s=80&y=2017&r=201780" + placeHolder + "&c=2";

            case 12:
                return "http://classic.autosport.com/results.php?s=82&y=2017&r=201780" + placeHolder + "&c=2";

            case 13:
                return "http://classic.autosport.com/results.php?s=83&y=2017&r=201780" + placeHolder + "&c=2";

            case 14:
                return "http://classic.autosport.com/results.php?s=710&y=2017&r=201771" + placeHolder + "&c=2";

            case 22:
                return "http://classic.autosport.com/results.php?s=623&y=2017&r=201761" + placeHolder + "&c=2";
        }

        return null;
    }

    // wtcc (double)
    // http://classic.autosport.com/results.php?s=70&y=2017&r=20177001&c=2
    // http://classic.autosport.com/results.php?s=70&y=2017&r=20177002&c=2

    // indy lights (double)
    // http://classic.autosport.com/results.php?s=110&y=2017&r=20171131&c=2

    // f2 (double)
    // http://classic.autosport.com/results.php?s=205&y=2017&r=20172001&c=2

    // dtm (double)
    // http://classic.autosport.com/results.php?s=72&y=2017&r=20177201&c=2

    // btcc (triple)
    // http://classic.autosport.com/results.php?s=701&y=2017&r=20177031&c=2

    // blancpain endurance
    // http://classic.autosport.com/results.php?s=6020&y=2017&r=20176081&c=2


    private static int getPlaceholderOffset(int id) {
        switch (id) {
            case 12:
            case 22:
                return 30;

            case 13:
                return 60;

            default:
                return 0;
        }
    }

    private static int getRacesPerEvent(int id) {
        switch (id) {
            case 5:
                return 2;
        }

        return 1;
    }

    public static boolean areResultsAvailable(int id) {
        String url = getUrl(id);
        return url != null && !url.isEmpty();
    }

    private static String getRaceIdentifier(int id, int raceNum) {
//        int offset = 0;
//        int racesPerEvent = getRacesPerEvent(id);
//        if (racesPerEvent > 1) {
//            offset = (raceNum - 1) * racesPerEvent;
//        }

        raceNum += getPlaceholderOffset(id);

        String sNum = String.valueOf(raceNum);
        if (sNum.length() == 1)
            sNum = "0" + sNum;

        return sNum;
    }


    public static String getUrl(int seriesId, int raceNum) {
        if (!areResultsAvailable(seriesId))
            return null;

        // TODO: 18/08/2017 chekc multiple races per event
        String url = getUrl(seriesId);
        int racePerEvent = getRacesPerEvent(seriesId);
        ArrayList<String> urls = new ArrayList<>();

        String raceIdentifier = getRaceIdentifier(seriesId, raceNum);
        if (raceIdentifier == null)
            return null;

        urls.add(url.replace(placeHolder, raceIdentifier));
        return urls.get(0);
    }

    public static ArrayList<EventResultUnit> getAutoSportResult(String data) {
        if (data == null || data.length() == 0)
            return null;

        ArrayList<EventResultUnit> results = new ArrayList<>();

        data = cleanBefore(data, "<table border=\"0\" cellpadding=\"3\" cellspacing=\"1\" width=\"100%\" bgcolor=\"#C0C0C0\" class=\"text_small\">");
        data = cleanAfter(data, "</table>");

        String[] arrHtmlPage = splitAndIgnorePositions(data, "<tr bgcolor=\"#FFFFFF\">", 2);
        if (arrHtmlPage == null)
            return null;

        for (String html : arrHtmlPage) {
            String position = getString(html, "<td align=\"center\">", "</td>");

            String name = getString(html, "<a target=", "/a>");
            name = getString(name, "\">", "<");

            String team = getStringAfterValue(html, name, "<td align=\"left\">", "</td>");
            String points = getString(html, "<td align=\"right\">", "</td></tr>", 1);

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
}

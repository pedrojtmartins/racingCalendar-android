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

            case 5:
                return "http://classic.autosport.com/results.php?s=70&y=2017&r=201770" + placeHolder + "&c=2";
        }

        return null;
    }

    public static int getRacePerEvent(int id) {
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

        String sNum = String.valueOf(raceNum);
        if (sNum.length() == 1)
            sNum = "0" + sNum;

        return sNum;
    }

    private static int getRacesPerEvent(int id) {
        switch (id) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 1;

            default:
                return -1;
        }
    }

    // f1
    // http://classic.autosport.com/results.php?s=0&y=2017&r=20170001&c=2
    // ...
    // http://classic.autosport.com/results.php?s=0&y=2017&r=20170010&c=2

    // fe
    // http://classic.autosport.com/results.php?s=202&y=2016&r=20160201&c=2

    // wtcc (double)
    // http://classic.autosport.com/results.php?s=70&y=2017&r=20177001&c=2
    // http://classic.autosport.com/results.php?s=70&y=2017&r=20177002&c=2

    // wrc
    // http://classic.autosport.com/results.php?s=901&y=2017&r=20170901&c=2

    // wec
    // http://classic.autosport.com/results.php?s=630&y=2017&r=20176091&c=2

    // nascar
    // http://classic.autosport.com/results.php?s=710&y=2017&r=20177101&c=2

    // motogp
    // http://classic.autosport.com/results.php?s=80&y=2017&r=20178001&c=2

    // moto2
    // http://classic.autosport.com/results.php?s=82&y=2017&r=20178031&c=2

    // moto3
    // http://classic.autosport.com/results.php?s=83&y=2017&r=20178061&c=2

    // indycar
    // http://classic.autosport.com/results.php?s=11&y=2017&r=20171101&c=2

    // indy lights (double)
    // http://classic.autosport.com/results.php?s=110&y=2017&r=20171131&c=2

    // imsa sportscar
    // http://classic.autosport.com/results.php?s=623&y=2017&r=20176131&c=2

    // f2 (double)
    // http://classic.autosport.com/results.php?s=205&y=2017&r=20172001&c=2

    // dtm (double)
    // http://classic.autosport.com/results.php?s=72&y=2017&r=20177201&c=2

    // btcc (triple)
    // http://classic.autosport.com/results.php?s=701&y=2017&r=20177031&c=2

    // blancpain endurance
    // http://classic.autosport.com/results.php?s=6020&y=2017&r=20176081&c=2

    public static String getUrl(int seriesId, int raceNum) {
        if (!areResultsAvailable(seriesId))
            return null;

        // TODO: 18/08/2017 chekc multiple races per event
        String url = getUrl(seriesId);
        int racePerEvent = getRacePerEvent(seriesId);
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
            String points = "";
//            String points = getString(html, "<td align=\"right\">", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
}

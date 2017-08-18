package com.pedrojtmartins.racingcalendar.eventResults;

import com.pedrojtmartins.racingcalendar.models.EventResultUnit;

import java.util.ArrayList;

import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.cleanAfter;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.cleanBefore;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.getString;
import static com.pedrojtmartins.racingcalendar.eventResults.ResultsHtmlHelper.getStringUntil;

/**
 * Pedro Martins
 * 26/06/2017
 */

public class SeriesResultsManager {
    public static String getUrl(int id) {
        switch (id) {
            case 1:
                return "https://www.driverdb.com/championships/standings/formula-1/2017";

            case 2:
                return "https://www.driverdb.com/championships/standings/fia-formula-e-championship/2017/";

            case 3:
                return "http://www.wrc.com/en/wrc/results/championship-standings/page/4176----.html";

            case 4:
                return "https://www.autosport.com/wec/standings";

            case 5:
                return "https://www.driverdb.com/championships/standings/wtcc/2017/";

            case 6:
                return "http://www.fiaworldrallycross.com/standings";

            case 7:
                return "https://www.driverdb.com/championships/standings/indycar-series/2017/";

            case 8:
                return "https://www.driverdb.com/championships/standings/indy-lights-295/2017/";

            case 9:
                return "https://www.driverdb.com/championships/standings/pro-mazda/2017/";

            case 10:
                return "https://www.driverdb.com/championships/standings/usf2000-national-championship/2017";

            case 11:
                return "http://www.gpupdate.net/en/standings/251/2017-motogp-standings/";

            case 12:
                return "http://www.gpupdate.net/en/standings/256/2017-moto2-standings/";

            case 13:
                return "http://www.gpupdate.net/en/standings/257/2017-moto3-standings/";

            case 14:
                return "https://www.driverdb.com/championships/standings/nascar-sprint-cup-series/2017/";

            case 15:
                return "https://www.driverdb.com/championships/standings/nascar-nationwide-series/2017/";

            case 16:
                return "https://www.driverdb.com/championships/standings/nascar-truck-series/2017/";

            case 17:
                return "https://www.driverdb.com/championships/standings/v8-supercar-championship-series/2017/";

            case 18:
                return "http://stadiumsupertrucks.com/results/standings/";

            case 19:
                return "https://www.driverdb.com/championships/standings/stock-car-brasil/2017/";

            case 20:
                return "https://www.driverdb.com/championships/standings/formula-truck/2017/";

            case 21:
                return "https://www.autosport.com/btcc/standings";

            case 22:
            case 23:
            case 24:
                return null;

            case 25:
                return "https://www.driverdb.com/championships/standings/european-touring-car-cup---s2000/2017/";

            case 26:
                return "https://www.driverdb.com/championships/standings/gp2-series/2017/";

            case 27:
                return null;

            case 28:
                return "https://www.driverdb.com/championships/standings/dtm/2017/";

            case 29:
                return "http://www.worldsbk.com/en/results%20statistics/riders%20manufacturers";


        }

        return null;
    }

    public static boolean areResultsAvailable(int id) {
        String url = getUrl(id);
        return url != null && !url.isEmpty();
    }

    //transam
    // https://www.driverdb.com/championships/standings/scca-trans-am/2017/
    // https://www.driverdb.com/championships/standings/trans-am---ta2/2017/

    //blancpain
    //  http://www.blancpain-gt-series.com/standings?filter_season_id=7&filter_standing_type=20_26_drivers

    // pirelli

    public static ArrayList<EventResultUnit> getStandings(String data, int id) {
        switch (id) {
            case 1:
            case 2:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 14:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 25:
            case 26:
            case 28:
                return getDriverDBStandings(data);

            case 3:
                return getWRCStandings(data);

            case 4:
            case 21:
                return getAutosportStandings(data);

            case 6:
                return getWRXstandings(data);

            case 11:
            case 12:
            case 13:
                return getMotoGPStandings(data);

            case 18:
                return getSSTruckstandings(data);

            case 29:
                return getWSBKstandings(data);
        }

        return null;
    }


//            case 1:
//                return getF1Standings(data);
//            case 2:
//                return getFEStandings(data);
//            case 14:
//            case 15:
//            case 16:
//                return getNASCARstandings(data);

    //region HTML helpers

    //endregion

    //region WRC
    // http://www.wrc.com/en/wrc/results/championship-standings/page/4176----.html
    public static ArrayList<EventResultUnit> getWRCStandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        String[] splitted = data.split(".png\" alt=\"\" title=\"\">");
        if (splitted.length == 0)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";

            String name = getStringUntil(html, "\n");
            String team = "";
            String points = getString(html, "<td class=\"aright highlight\">", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
    //endregion

    //region Misc
    // https://www.autosport.com/wec/standings
    // https://www.autosport.com/dtm/standings
    // https://www.autosport.com/btcc/standings
    // https://www.autosport.com/wtcc/standings
    // https://www.autosport.com/wrc/standings
    // https://www.autosport.com/f2/standings
    // https://www.autosport.com/nascar/standings
    public static ArrayList<EventResultUnit> getAutosportStandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        String[] splitted = data.split("<td class=\"position\">");
        if (splitted.length == 0)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);


        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = getStringUntil(html, "</td>");

            String name = getString(html, "<a href=\"", "/a>");
            name = getString(name, "\">", "<");

            String team = "";

            String points = getString(html, "</a></td>\n" +
                    "\t\t\t\t\t<td>", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }

    private static ArrayList<EventResultUnit> getDriverDBStandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        data = cleanAfter(data, "</table>");
        String clean = cleanBefore(data, "<small>Points</small>");

        if (clean == null)
            return null;

        String[] splitted = clean.split("</small></td></tr>\n</tr>");
        if (splitted.length <= 1)
            return null;

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : splitted) {
            splitted = html.split("<td>");
            if (splitted.length <= 1)
                return null;

            String position = getString(splitted[1], "<small>", ".</small>");
            String name = getString(splitted[4], "\">", "</a>");
            String team = getString(splitted[5], "<small>", "</small>");
            String points = getString(splitted[9], "\">", "</div>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }

    //endregion

    //region stadiumsupertrucks
    // http://stadiumsupertrucks.com/results/standings/
    public static ArrayList<EventResultUnit> getSSTruckstandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        data = cleanAfter(data, "</tbody>");
        String clean = cleanBefore(data, "<tbody>");

        if (clean == null)
            return null;

        String[] arrHtmlPage = clean.split("</tr>\n" + " {36}<tr>");
        if (arrHtmlPage.length <= 1)
            return null;

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";

            String[] splitted = html.split("<td align=\"Left\">");
            if (splitted.length <= 3)
                continue;

            String name = getStringUntil(splitted[2], "</td>");
            String team = "";
            String points = getStringUntil(splitted[3], "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
    //endregion

    //region WORLD RX
    // http://www.fiaworldrallycross.com/standings
    public static ArrayList<EventResultUnit> getWRXstandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        data = cleanAfter(data, "</tbody>");
        String clean = cleanBefore(data, "<tbody>");

        if (clean == null)
            return null;

        String[] splitted = clean.split("<span class=\"driver\"");
        if (splitted.length <= 1)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {

            if (html.startsWith(">\n"))
                html = html.substring(6);

            String position = (results.size() + 1) + "";

            String startLimiter = html.startsWith("\">") ? "\">" : ">";
            String endLimiter = html.startsWith("\">") ? "</span>" : "</span";
            String name = getString(html, startLimiter, endLimiter);
            name = name.trim();

            String team = "";
            String points = getString(html, "<td class=\"points\">", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
    //endregion

    //region WSBK
    // http://www.worldsbk.com/en/results%20statistics/riders%20manufacturers
    public static ArrayList<EventResultUnit> getWSBKstandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        data = cleanAfter(data, "</tbody>");
        String clean = cleanBefore(data, "<tbody>");

        if (clean == null)
            return null;

        String[] splitted = clean.split("&nbsp;</td>");
        if (splitted.length <= 1)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";

            String name = getString(html, "\">", "</a>");
            String team = "";

            String[] arrPoints = html.split("<td>&nbsp;");
            String points = getStringUntil(arrPoints[3], "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
    //endregion

    //region MotoGP
    // MotoGP - http://www.gpupdate.net/en/standings/251/2017-motogp-standings/
    // Moto2  - http://www.gpupdate.net/en/standings/256/2017-moto2-standings/
    // Moto3  - http://www.gpupdate.net/en/standings/257/2017-moto3-standings/
    public static ArrayList<EventResultUnit> getMotoGPStandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        String[] splitted = data.split("<td class=\"number right\">");
        if (splitted.length == 0)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);

        boolean firstReading = false;
        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            if (html.startsWith("1.&nbsp;")) {
                if (firstReading)
                    break;
                else
                    firstReading = true;
            }

            String position = (results.size() + 1) + "";

            String name = getString(html, "<a href=\"http://www.gpupdate.net/en", "</a>");
            name = getString(name, "\">");

            String team = getString(html, "</a>\n                                                    </td>\n                                                    <td>", "</td>");
            String points = getString(html, "<td class=\"last right number\">", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
//endregion
}

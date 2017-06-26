package com.pedrojtmartins.racingcalendar.eventResults;

import com.pedrojtmartins.racingcalendar.models.EventResultUnit;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 26/06/2017
 */

public class EventResultsParser {
    public static String getUrl(int id) {
        switch (id) {
            case 1:
                return "https://www.formula1.com/en/results.html/2017/drivers.html";

            case 3:
                return "http://www.wrc.com/en/wrc/results/championship-standings/page/4176----.html";

            case 4:
                return "https://www.autosport.com/wec/standings";

            case 5:
                return "https://www.autosport.com/wtcc/standings";

            case 11:
                return "http://www.gpupdate.net/en/standings/251/2017-motogp-standings/";

            case 12:
                return "http://www.gpupdate.net/en/standings/256/2017-moto2-standings/";

            case 13:
                return "http://www.gpupdate.net/en/standings/257/2017-moto3-standings/";

            case 21:
                return "https://www.autosport.com/btcc/standings";

            case 26:
                return "https://www.autosport.com/f2/standings";

            case 28:
                return "https://www.autosport.com/dtm/standings";

        }

        return null;
    }

    //region HTML helpers
    private static String[] cleanBefore(String source, String cleanFilter) {
        String[] clean = source.split(cleanFilter);
        if (clean.length <= 1)
            return null;

        String[] cleaner = new String[clean.length - 1];
        System.arraycopy(clean, 1, cleaner, 0, clean.length - 1);

        return cleaner;
    }

    private static String cleanAfter(String source, String cleanFilter) {
        String[] clean = source.split(cleanFilter);
        if (clean.length == 0)
            return null;


        return clean[0];
    }

    private static String getString(String source, String startLimiter, String endLimiter) {
        String[] arr = source.split(startLimiter);
        if (arr.length >= 2) {
            return arr[1].substring(0, arr[1].indexOf(endLimiter));
        }
        return "";
    }

    private static String getString(String source, String startLimiter) {
        String[] arr = source.split(startLimiter);
        if (arr.length > 1) {
            return arr[1];
        }
        return "";
    }

    private static String getStringUntil(String source, String endLimiter) {
        String[] arr = source.split(endLimiter);
        if (arr.length >= 1) {
            return arr[0];
        }
        return "";
    }
    //endregion

    public static ArrayList<EventResultUnit> getStandings(String data, int id) {
        switch (id) {
            case 1:
                return getF1Standings(data);

            case 3:
                return getWRCStandings(data);

            case 4:
            case 5:
            case 21:
            case 26:
            case 28:

                return getAutosprtStandings(data);

            case 11:
            case 12:
            case 13:
                return getMotoGPStandings(data);


        }

        return null;
    }

    //region F1  -  https://www.formula1.com/en/results.html/2017/drivers.html
    public static ArrayList<EventResultUnit> getF1Standings(String data) {
        if (data == null || data.length() == 0)
            return null;

        String[] arrHtmlPage = cleanBefore(data, "\"hide-for-tablet\">");
        if (arrHtmlPage == null)
            return null;

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";
            String name = getF1NamesString(html, "</span>\n {20}<span class=\"hide-for-mobile\">", "</span>");
            String team = getString(html, "class=\"grey semi-bold uppercase ArchiveLink\">", "</a>");
            String points = getString(html, "class=\"dark bold\">", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }

    private static String getF1NamesString(String source, String startLimiter, String endLimiter) {
        String[] arrNames = source.split(startLimiter);
        if (arrNames.length >= 2) {
            return arrNames[0] + " " + arrNames[1].substring(0, arrNames[1].indexOf(endLimiter));
        }
        return "";
    }
    //endregion

//    //region Fe  -  http://forix-proxy-prod.formulae.corebine.com/fe_server.php?championship=2022016&page=drivers-standings
//    public static ArrayList<EventResultUnit> getFEStandings(FESerialization serialized) {
//        if (serialized == null
//                || serialized.serieData == null
//                || serialized.serieData.championshipStanding == null
//                || serialized.serieData.championshipStanding.championshipData == null
//                || serialized.serieData.championshipStanding.championshipData.standings == null)
//            return null;
//
//        ArrayList<EventResultUnit> results = new ArrayList<>();
//        for (FESerialization.Standing standing : serialized.serieData.championshipStanding.championshipData.standings) {
//            results.add(new EventResultUnit(standing.pos, standing.driverName, standing.teamName, standing.totalPoints));
//        }
//        return results;
//    }
//    //endregion

//    //region Indycar
//    // Verizon Indy Car -  http://www.indycar.com/Services/IndyStats.svc/YearPointSummary?year=2017&id=b856a4f1-e85c-4fac-8c36-fd58d962227a
//    // Indy Lights      -  http://www.indycar.com/Services/IndyStats.svc/YearPointSummary?year=2017&id=09341e09-3216-4f89-a45f-db697d72ee13
//    // Pro Mazda        -  http://www.indycar.com/Services/IndyStats.svc/YearPointSummary?year=2017&id=e7d22e50-e006-4a28-8bb7-37acca1148ae
//    // USF2000          -  http://www.indycar.com/Services/IndyStats.svc/YearPointSummary?year=2017&id=d96a0dcb-47df-4a66-947c-0ea6777a676b
//    public static ArrayList<EventResultUnit> getIndycarStandings(IndyCarSerialization serialized) {
//        if (serialized == null || serialized.driverList == null || serialized.driverList.isEmpty())
//            return null;
//
//        ArrayList<EventResultUnit> results = new ArrayList<>();
//        for (IndyCarSerialization.DriverList standing : serialized.driverList) {
//            results.add(new EventResultUnit(standing.overallPosition, standing.driverName, standing.totalPoints));
//        }
//        return results;
//    }
//    //endregion

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
    public static ArrayList<EventResultUnit> getAutosprtStandings(String data) {
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
    //endregion

    //region FÃ“RMULA TRUCK BR
    // http://www.formulatruck.com.br/classificacao-tabela.asp?temporada=8&campeonato=3
    public static ArrayList<EventResultUnit> getFormulaTruckBRtandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        String[] arrHtmlPage = cleanBefore(data, "<td align=\"left\" valign=\"middle\">");
        if (arrHtmlPage == null || arrHtmlPage.length == 0)
            return null;

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";

            String name = getStringUntil(html, "</td>");
            String team = "";
            String points = getString(html, "<strong>", "</strong>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
    //endregion

    //region StockCar BR
    // http://www.stockcar.com.br/Classificacao/
    public static ArrayList<EventResultUnit> getStockCarBRtandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        data = cleanAfter(data, "conteudo_tabela_temporada_equipes");
        String[] clean = cleanBefore(data, "tabela_temporadaPilotos_ul");

        if (clean == null || clean.length == 0)
            return null;

        String[] splitted = clean[0].split("<a href='/Pilotos/");
        if (splitted.length <= 1)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";

            String name = getString(html, "<h3>", "</h3>");
            String team = "";
            String points = getString(html, "<span class='col pontos'>", "</span>");

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
        String[] clean = cleanBefore(data, "<tbody>");

        if (clean == null || clean.length == 0)
            return null;

        String[] arrHtmlPage = clean[0].split("</tr>\n" + " {36}<tr>");
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
        String[] clean = cleanBefore(data, "<tbody>");

        if (clean == null || clean.length == 0)
            return null;

        String[] splitted = clean[0].split("<span class=\"driver\"");
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
        String[] clean = cleanBefore(data, "<tbody>");

        if (clean == null || clean.length == 0)
            return null;

        String[] splitted = clean[0].split("&nbsp;</td>");
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

    //region NASCAR
    // http://www.foxsports.com/nascar/standings?circuit=3&season=2017 - XFINITY
    // http://www.foxsports.com/nascar/standings?circuit=2&season=2017 - Monster Energy
    // http://www.foxsports.com/nascar/standings?circuit=4&season=2017 - Camping
    public static ArrayList<EventResultUnit> getNASCARstandings(String data) {
        if (data == null || data.length() == 0)
            return null;

        data = cleanAfter(data, "</tbody>");
        String[] clean = cleanBefore(data, "<tbody>");

        if (clean == null || clean.length == 0)
            return null;

        String[] splitted = clean[0].split("<div class=\"wisbb_fullPlayer\">");
        if (splitted.length <= 1)
            return null;

        String[] arrHtmlPage = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arrHtmlPage, 0, arrHtmlPage.length);

        ArrayList<EventResultUnit> results = new ArrayList<>();
        for (String html : arrHtmlPage) {
            String position = (results.size() + 1) + "";

            String name = getString(html, "<span>", "</span>");
            String team = "";
            String points = getString(html, "<td class=\"wisbb_priorityColumn\">", "</td>");

            results.add(new EventResultUnit(position, name, team, points));
        }

        return results;
    }
    //endregion

}

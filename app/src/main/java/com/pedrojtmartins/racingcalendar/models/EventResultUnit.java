package com.pedrojtmartins.racingcalendar.models;

/**
 * Pedro Martins
 * 26/06/2017
 */

public class EventResultUnit {
    public String position;
    public String name;
    public String team;
    public String points;

    public EventResultUnit(String position, String name, String team, String points) {
        this.position = position;
        this.name = name;
        this.team = team;
        this.points = points;
    }

    public EventResultUnit(int position, String name, int points) {
        this.position = position + "";
        this.name = name;
        this.team = "";
        this.points = points + "";
    }

    @Override
    public String toString() {
        return position + " - " + name + " - " + team + " - " + points + "\n";
    }

}

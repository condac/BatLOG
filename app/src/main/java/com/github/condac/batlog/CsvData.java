package com.github.condac.batlog;

/**
 * Created by burns on 3/23/18.
 */

public class CsvData {

    public int cycleNr;
    public long date;
    public int charge;
    public int discharge;
    public int totalCap;
    public int resistance;

    public CsvData(int cycleNr, long date, int charge, int discharge, int totalCap, int resistance) {
        this.cycleNr = cycleNr;
        this.date = date;
        this.charge = charge;
        this.discharge = discharge;
        this.totalCap = totalCap;
        this.resistance = resistance;
    }
}

package fr.warzou.databaselib.task;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class ActionClock {

    private long start;
    private long end;

    public void start() {
        this.start = new Date().getTime();
    }

    public void stop() {
        this.end = new Date().getTime();
    }

    public long difference() {
        return this.end - this.start;
    }

    public String toSeconds() {
        NumberFormat numberFormat = new DecimalFormat("#0.000", new DecimalFormatSymbols(Locale.US));
        return numberFormat.format(difference() / 1000.0d);
    }

}

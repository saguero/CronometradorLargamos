package silvana.com.manual.clasificador;

import android.content.Context;

import java.util.Date;

import silvana.com.manual.Main2Activity;

/**
 * Created by silvana-aguero on 07/02/2018.
 *
 * A simple Runnable class to generate time difference since a starting time in milliseconds
 */

public class Cronometro implements Runnable {

    private static final long MILLIS_TO_MINUTES = 60000;
    private static final long MILLS_TO_HOURS = 3600000;

    private static TabCronometro tabCronometro;

    private Context mContext;

    private long mStartTime;

    private boolean mIsRunning;


    public Cronometro(TabCronometro tabCronometro, long startTime) {
        this.tabCronometro = tabCronometro;
       // mContext = context;
        mStartTime = startTime;
    }

    public void start() {
        mIsRunning = true;
    }

    public void stop() {
        mIsRunning = false;
    }


    @Override
    public void run() {
        while(mIsRunning) {
            long since = System.currentTimeMillis() - mStartTime;

            int seconds = (int) (since / 1000) % 60;
            int minutes = (int) ((since / (MILLIS_TO_MINUTES)) % 60);
            int hours = (int) ((since / (MILLS_TO_HOURS))); //this does not reset to 0!
            int millis = (int) since % 1000; //the last 3 digits of millisecs

          ( tabCronometro).updateTimerText(String.format("%02d:%02d:%02d.%03d"
                    , hours, minutes, seconds, millis));

            //Sleep the thread for a short amount, to prevent high CPU usage!
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFormatTime(Long now) {
        long since = now - mStartTime;
        int seconds = (int) (since / 1000) % 60;
        int minutes = (int) ((since / (MILLIS_TO_MINUTES)) % 60);
        int hours = (int) ((since / (MILLS_TO_HOURS))); //this does not reset to 0!
        int millis = (int) since % 1000; //the last 3 digits of millisecs
        return String.format("%02d:%02d:%02d.%03d"
                , hours, minutes, seconds, millis);
    }

    public Date getDateStart() {
        Date d = new Date();
        d.setTime(mStartTime);
        return d;
    }
}

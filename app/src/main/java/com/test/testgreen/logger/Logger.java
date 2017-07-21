package com.test.testgreen.logger;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

    private static final String PATH = "sdcard/log.dat";
    private static final boolean DEBUG = true;

    public static void d(String tag, String massage) {
        Log.d(tag, massage);
        if (DEBUG) {
            write(tag, massage);
        }
    }

    public static void v(String tag, String massage) {
        Log.v(tag, massage);
        if (DEBUG) {
            write(tag, massage);
        }
    }

    public static void i(String tag, String massage) {
        Log.i(tag, massage);
        if (DEBUG) {
            write(tag, massage);
        }
    }

    public static void e(String tag, String massage) {
        Log.e(tag, massage);
        if (DEBUG) {
            write(tag, massage);
        }
    }

    public static void w(String tag, String massage) {
        Log.w(tag, massage);
        if (DEBUG) {
            write(tag, massage);
        }
    }

    public static void write(String tag, String message) {
        File file = new File(PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String timeLog = new SimpleDateFormat("dd.MM.yy hh:mm:ss").format(new Date());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(timeLog + " (" + tag + ")\t" + message + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

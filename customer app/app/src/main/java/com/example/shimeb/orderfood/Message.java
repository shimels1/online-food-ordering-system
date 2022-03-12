package com.example.shimeb.orderfood;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by shime on 12/1/2017.
 */

public class Message {
    public static void log(String text) {
        Log.i("log >->->->->->->->", text);
    }

    public static void message(String text) {
        Toast.makeText(MainActivity.context, text, Toast.LENGTH_SHORT).show();
    }
}

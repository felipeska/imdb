package com.arandasoft.android.imdb.log;

import com.arandasoft.android.imdb.app.ImdbApp;
import android.util.Log;

/**
 * Clase que simplifica el uso de Logs dentro de la aplicacion
 *
 * @author Felipe Calderon <felipeskabarragan@gmail.com>
 * */
public class Logger {

    public static void d(String message) {
        if (ImdbApp.getLogStatus())
            Log.d(ImdbApp.getLogName(), message);
    }

    public static void i(String message) {
        Log.i(ImdbApp.getLogName(), message);
    }

    public static void e(final String message, Throwable e) {
        if (e != null)
            Log.e(ImdbApp.getLogName(), message, e);
        else
            Log.e(ImdbApp.getLogName(), message);
    }
}

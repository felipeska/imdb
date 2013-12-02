/*
 * WideTechLogger.java
 *
 * Created on December 11, 2012
 */

package com.arandasoft.android.imdb.log;

import com.arandasoft.android.imdb.app.ImdbApp;
import android.util.Log;

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
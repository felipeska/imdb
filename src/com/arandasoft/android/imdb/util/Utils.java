package com.arandasoft.android.imdb.util;

public class Utils {

	public static String cleanTitle(String rawTitle) {
		StringBuilder t = new StringBuilder();
		String split[] = rawTitle.split("\n");

		if (split.length > 1) {
			t.append(split[0]);
		} else
			t.append(rawTitle);
		return t.toString();
	}

	public static String cropUrl(String url) {
		StringBuilder newUrl = new StringBuilder();
		String split[] = url.split("._V1_");

		if (split.length > 1) {
			newUrl.append(split[0].concat("._V1_SX800_.jpg"));
		} else
			newUrl.append(url);

		return newUrl.toString();
	}
}

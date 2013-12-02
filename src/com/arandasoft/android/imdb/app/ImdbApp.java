package com.arandasoft.android.imdb.app;

import java.io.File;
import java.io.IOException;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import com.arandafost.android.imdb.rest.SearchService;
import com.arandasoft.android.imdb.api.ImdbAPI;
import com.arandasoft.android.imdb.log.Logger;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import android.app.Application;

/**
 * Clase para mantener el estado global de la aplicacion e inicializar los
 * componentes para realizar peticiones tipo REST, tratamiento de imagenes y
 * creacion de cache
 * */
public class ImdbApp extends Application {

	private static boolean logEnabled = true; // Activamos lo logs
	private static String logName = "imdbArandaSoft";
	public static final int CACHE_SIZE = 10 * 1024 * 1024;
	public static final String NAME_CACHE = "idmb-http-cache";
	private RestAdapter restAdapter;
	private Picasso picasso;
	private OkHttpClient okHttpClient;

	private SearchService searchService;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.d("App iniciada");
		restAdapter = new RestAdapter.Builder().setServer(ImdbAPI.API_URL)
				.setLog(new RestAdapter.Log() {

					@Override
					public void log(String log) {
						Logger.d(log);
					}
				}).setLogLevel(LogLevel.FULL).build();
		searchService = restAdapter.create(SearchService.class);

		okHttpClient = new OkHttpClient();
		try {
			okHttpClient.setResponseCache(new HttpResponseCache(new File(
					getCacheDir(), NAME_CACHE), CACHE_SIZE));
		} catch (IOException e) {
			Logger.e("Error al crear el cache: ", e);
		}

		picasso = new Picasso.Builder(this).downloader(
				new OkHttpDownloader(okHttpClient)).build();
		picasso.setDebugging(true);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Logger.d("App terminada");
	}

	public static boolean getLogStatus() {
		return logEnabled;
	}

	public static String getLogName() {
		return logName;
	}

	public RestAdapter getRestAdapter() {
		return restAdapter;
	}

	public Picasso getPicasso() {
		return picasso;
	}

	public OkHttpClient getHttpClient() {
		return okHttpClient;
	}

	public SearchService getSearchService() {
		return searchService;
	}
}
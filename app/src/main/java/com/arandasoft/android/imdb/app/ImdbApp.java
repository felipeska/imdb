package com.arandasoft.android.imdb.app;

import java.io.File;
import java.io.IOException;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import com.arandasoft.android.imdb.api.ImdbAPI;
import com.arandasoft.android.imdb.log.Logger;
import com.arandasoft.android.imdb.rest.SearchService;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import android.app.Application;

/**
 * Clase para mantener el estado global de la aplicacion e inicializar los
 * componentes para realizar peticiones tipo REST, tratamiento de imagenes y
 * creacion de cache
 *
 * @author Felipe Calderon <felipeskabarragan@gmail.com>
 * */
public class ImdbApp extends Application {

    /* Activamos los logs */
    private static boolean logEnabled = true;
    /* Tag para identificar los Logs de la aplicacion */
    private static String logName = "imdbArandaSoft";
    /* tam para el cache */
    public static final int CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    /*
     * Nombre de el almacenamiento de cache
     */
    public static final String NAME_CACHE = "idmb-http-cache";
    /*
     * Adaptador rest para realizar REST al API
     */
    private RestAdapter restAdapter;
    /*
     * Mediante el objeto Picasso podemos descargar y cachear las imagenes
     */
    private Picasso picasso;
    /*
     * Mediante el objeto OkHttpClient creamos un cliente que administre las
     * descargas y el cache de las imagenes
     */
    private OkHttpClient okHttpClient;
    /*
     * Interfaz para acceder al las busquedas de peliculas o series
     */
    private SearchService searchService;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("App iniciada");
		/*
		 * Cuando construimos el RestAdapter podemos indicar el nivel de Logs
		 * que necesitamos y una interfaz para acceder a los mismos
		 */
        restAdapter = new RestAdapter.Builder().setServer(ImdbAPI.API_URL)
                .setLog(new RestAdapter.Log() {

                    @Override
                    public void log(String log) {
                        Logger.d(log);
                    }
                }).setLogLevel(LogLevel.FULL).build();

		/* implementamos la interfaz SearchService con el RestAdapter */
        searchService = restAdapter.create(SearchService.class);

		/*
		 * Creamos el cliente OkHttp y lo asignamos para que genere el cache de
		 * las imagenes
		 */
        okHttpClient = new OkHttpClient();
        try {
            okHttpClient.setResponseCache(new HttpResponseCache(new File(
                    getCacheDir(), NAME_CACHE), CACHE_SIZE));
        } catch (IOException e) {
            Logger.e("Error al crear el cache: ", e);
        }

		/*
		 * Cuando creamos el objeto Picasso nos solicita un cliente para
		 * encargarse de la gestion de descarga y cache de imagenes
		 */
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

    public Picasso getPicasso() {
        return picasso;
    }

    public SearchService getSearchService() {
        return searchService;
    }
}

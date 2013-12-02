package com.arandasoft.android.imdb.api;

import java.util.List;
import retrofit.Callback;
import android.app.Activity;
import com.arandasoft.android.imdb.app.ImdbApp;
import com.arandasoft.android.imdb.bean.Movie;
import com.arandasoft.android.imdb.rest.SearchService;

/**
 * En esta clase se encuentran los metodos para realizar peticiones de datos al
 * API y lectura de los datos en formato JSON que esta retorna
 * **/
public class ImdbAPI {

	public static String API_URL = "http://mymovieapi.com/"; // URL de accesso
																// al API
	public static String VALUE_TYPE_DATA = "json";
	public static String VALUE_PLOT = "full";
	public static int VALUE_EPISODE = 0;
	public static int VALUE_LIMIT = 10;
	public static int VALUE_YG = 0;
	public static String VALUE_MT = "none";
	public static String VALUE_LANGUAGE = "en-US";
	public static String VALUE_OFFSET = "";
	public static String VALUE_AKA = "simple";
	public static String VALUE_RELEASE = "simple";
	public static int VALUE_BUSINESS = 0;
	public static int VALUE_TECH = 0;

	/**
	 * metodo para acceder al RestAdapter y a partir de la interfaz de
	 * {@linkplain com.arandasoft.android.imdb.rest.SearchService} se ejecuta la
	 * accion de busqueda o peticion al API
	 * 
	 * @param param
	 *            el nombre de la pelicula o serie a buscar.
	 * @param target
	 *            la actividad desde la cual es invocada.
	 * @param result
	 *            {@link Callback} para seguir el estado de la peticion
	 * */
	public static void search(String param, Activity target,
			Callback<List<Movie>> result) {

		SearchService service = ((ImdbApp) target.getApplication())
				.getSearchService();
		service.listSearch(param, VALUE_TYPE_DATA, VALUE_PLOT, VALUE_EPISODE,
				VALUE_LIMIT, VALUE_YG, VALUE_MT, VALUE_LANGUAGE, VALUE_OFFSET,
				VALUE_AKA, VALUE_RELEASE, VALUE_BUSINESS, VALUE_TECH, result);
	}

}

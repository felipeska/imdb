package com.arandafost.android.imdb.rest;

import java.util.List;
import com.arandasoft.android.imdb.bean.Movie;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface SearchService {

	String TITLE = "title"; // titulo de la pelicula o serie a buscar.
	String PARAM_FORMAT_DATA = "type"; // tipo de data de retorno (JSON, XML).
	String PARAM_PLOT = "plot";
	String PARAM_EPISODE = "episode";
	String PARAM_LIMIT = "limit";
	String PARAM_YEAR_STATUS = "yg";
	String PARAM_MOVIE_TYPE = "mt";
	String PARAM_DATA_LANGUAGE = "lang"; // idioma en el que retorna la info
	String PARAM_OFFSET = "offset"; // indice de paginacion
	String PARAM_AKA = "aka";
	String PARAM_RELEASE = "release";
	String PARAM_BUSINESS = "business";
	String PARAM_TECHNICAL = "technical";

	@GET("/")
	void listSearch(@Query(TITLE) String title,
			@Query(PARAM_FORMAT_DATA) String type,
			@Query(PARAM_PLOT) String plot, @Query(PARAM_EPISODE) int episode,
			@Query(PARAM_LIMIT) int limit, @Query(PARAM_YEAR_STATUS) int yg,
			@Query(PARAM_MOVIE_TYPE) String mt,
			@Query(PARAM_DATA_LANGUAGE) String language,
			@Query(PARAM_OFFSET) String offset, @Query(PARAM_AKA) String aka,
			@Query(PARAM_RELEASE) String release,
			@Query(PARAM_BUSINESS) int business,
			@Query(PARAM_TECHNICAL) int technical, Callback<List<Movie>> result);
}

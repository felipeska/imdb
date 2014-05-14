package com.arandasoft.android.imdb.bean;

/**
 * Clase para mapear los JsonObject principales de la busqueda. El 
 * Converter de Retrofit hace el trabajo sucio por nosotros :-)
 *
 * @author Felipe Calderon <felipeskabarragan@gmail.com>
 * */
public class Movie {

    public String title;
    public String imdb_id;
    public String year;
    public String rating;
    public String plot;
    public Poster poster;
}

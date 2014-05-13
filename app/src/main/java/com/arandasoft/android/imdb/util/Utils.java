package com.arandasoft.android.imdb.util;

/**
 * <p>
 * Clase en la cual se crean algunas utilerias que se usan en toda la aplicacion
 * </p>
 * 
 * @author Felipe Calderon <felipeskabarragan@gmail.com>
 */
public class Utils {

	/**
	 * Metodo para cortar el titulo de la pelicula o serie
	 * 
	 * @param {@link String} rawTitle el titulo repetido
	 * @return {@link String} t con el titulo cortado
	 */
	public static String cleanTitle(String rawTitle) {
		StringBuilder t = new StringBuilder();
		String split[] = rawTitle.split("\n");

		if (split.length > 1) {
			t.append(split[0]);
		} else
			t.append(rawTitle);
		return t.toString();
	}

	/**
	 * Metodo para cortar la url y cambiar el tipo de imagen que trae por
	 * defecto. con esto podemos conseguir imagenes en la API de mejor calidad
	 * 
	 * @param {@link StringBuilder} url de la cual podemos obtener la imagen de
	 *        cover de la serie o pelicula
	 * 
	 * @return {@link StringBuilder} newUrl la cual nos proporciona una imagen
	 *         de mejor calidad
	 * 
	 * */
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
package com.arandasoft.android.imdb;

import java.util.List;
import java.util.regex.Pattern;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.arandasoft.android.imdb.adapter.ResultAdapter;
import com.arandasoft.android.imdb.api.ImdbAPI;
import com.arandasoft.android.imdb.bean.Movie;
import com.arandasoft.android.imdb.util.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * <p>
 * Vista principal en la que el usuario puede realizar la busqueda de una serie
 * o pelicula.
 * </p>
 * 
 * @author Felipe Calderon <felipeskabarragan@gmail.com>
 * */
public class MainActivity extends SherlockFragmentActivity {

	private ListView mListResult;
	private ResultAdapter mAdapterResult;
	private EditText mEditTextSearch;
	private ImageButton mButtonSearch;
	private ProgressDialog mProgressSearch;
	private String mParamSearch;

	/* validacion para que no ingresen en la busqueda caracteres especiales */
	Pattern pattern = Pattern.compile("[^\\d,\\w,\\s]");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* Logo y titulo del actionbar */
		getSupportActionBar().setTitle(R.string.title_search_movies);
		getSupportActionBar().setLogo(R.drawable.logo);

		/* Componentes de tipo UI de la interfaz grafica */
		mEditTextSearch = (EditText) findViewById(R.id.dashboard_search_field);
		mButtonSearch = (ImageButton) findViewById(R.id.search_button);
		mListResult = (ListView) findViewById(R.id.results);
		mButtonSearch.setOnClickListener(mListenerSearchButton);
		mEditTextSearch.setOnEditorActionListener(mListenerDoneAction);
		mListResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {

				/* Intent el cual nos muestra el detalle de la pelicula o serie */
				Intent intentResume = new Intent(MainActivity.this,
						DetailFilmActivity.class);

				/*
				 * Paso de dato a traves del intent, en este caso el titulo de
				 * la pelicula o serie
				 */
				intentResume.putExtra(
						DetailFilmActivity.KEY_TITLE,
						Utils.cleanTitle(((Movie) mAdapterResult.getItem(pos)).title));

				/*
				 * Paso de dato a traves del intent, en este caso el id de idbm
				 * de la pelicula o serie
				 */
				intentResume.putExtra(DetailFilmActivity.KEY_IMDB_ID,
						((Movie) mAdapterResult.getItem(pos)).imdb_id);
				intentResume.putExtra(DetailFilmActivity.KEY_RESUME,
						((Movie) mAdapterResult.getItem(pos)).plot);

				/* Validando que el objeto de tipo Movie tenga poster */
				if (((Movie) mAdapterResult.getItem(pos)).poster != null) {

					/*
					 * Paso de dato a traves del intent, en este caso la url del
					 * cover de la pelicula o serie
					 */
					intentResume
							.putExtra(DetailFilmActivity.KEY_URL_POSTER, Utils
									.cropUrl(((Movie) mAdapterResult
											.getItem(pos)).poster.imdb));
				}
				startActivity(intentResume);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private final View.OnClickListener mListenerSearchButton = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			validateSearch();
		}
	};

	/**
	 * Por medio de esta interfaz de clase, validamos que cuando el usuario
	 * presiona 'done' en el teclado ejecutemos una accion
	 */
	private final TextView.OnEditorActionListener mListenerDoneAction = new TextView.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
					|| (actionId == EditorInfo.IME_ACTION_DONE)) {
				validateSearch();
			}
			return false;
		}
	};

	/**
	 * metodo que valida si el campo de busqueda es nulo y si contiene
	 * caracteres especiales los cuales no son validos como parametro
	 * 
	 * */
	private void validateSearch() {

		/* el nombre de la pelicula o serie a buscar */
		mParamSearch = mEditTextSearch.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mParamSearch)) {
			focusView = mEditTextSearch;
			cancel = true;
			/*
			 * validamos que el parametro de busqueda no contenga caracteres
			 * especiales
			 */
		} else if ((pattern.matcher(mParamSearch).find())) {
			Toast.makeText(this, getString(R.string.no_special_characters),
					Toast.LENGTH_LONG).show();
			focusView = mEditTextSearch;
			cancel = true;
		}

		if (cancel)
			focusView.requestFocus();
		else {
			progressDialog("Buscando peliculas");
			startSearch();
		}
	}

	/**
	 * metodo que se encarga de realizar la peticion al API de
	 * http://mymovieapi.com/
	 * 
	 * */
	private void startSearch() {
		ImdbAPI.search(mParamSearch, this, new Callback<List<Movie>>() {
			@Override
			public void success(List<Movie> result, Response response) {

				mProgressSearch.cancel();
				mAdapterResult = new ResultAdapter(result, MainActivity.this);
				mListResult.setAdapter(mAdapterResult);
				updateView(true);
			}

			@Override
			public void failure(RetrofitError error) {
				mProgressSearch.cancel();
				updateView(false);
				findViewById(R.id.welcomeSearch).setVisibility(View.GONE);
				findViewById(R.id.noData).setVisibility(View.VISIBLE);
			}
		});
	}

	private void updateView(boolean status) {

		if (status) {
			findViewById(R.id.welcomeSearch).setVisibility(View.GONE);
			if (findViewById(R.id.noData).getVisibility() == View.VISIBLE)
				findViewById(R.id.noData).setVisibility(View.GONE);
		} else {
			findViewById(R.id.noData).setVisibility(View.VISIBLE);
			if (findViewById(R.id.welcomeSearch).getVisibility() == View.VISIBLE)
				findViewById(R.id.welcomeSearch).setVisibility(View.GONE);

			if (mListResult.getAdapter() != null) {
				((ResultAdapter)mListResult.getAdapter()).clear();
			}
		}
	}

	/**
	 * metodo el cual nos permite visualizar un progressDialog para informar al
	 * usuario que se esta realizando una busqueda
	 */
	private void progressDialog(String message) {
		mProgressSearch = new ProgressDialog(this, R.style.ImdbProgressDialog);
		mProgressSearch.setMessage(message);
		mProgressSearch.setCancelable(false);
		mProgressSearch.setIndeterminate(true);
		mProgressSearch.show();
	}
}

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
import com.arandasoft.android.imdb.log.Logger;
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
 * Vista principal en la que el usuario puede realizar la busqueda de una serie
 * o pelicula
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

		mEditTextSearch = (EditText) findViewById(R.id.dashboard_search_field);
		mButtonSearch = (ImageButton) findViewById(R.id.search_button);
		mListResult = (ListView) findViewById(R.id.results);
		mButtonSearch.setOnClickListener(mListenerSearchButton);
		mEditTextSearch.setOnEditorActionListener(mListenerDoneAction);
		mListResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {

				Intent intentResume = new Intent(MainActivity.this,
						DetailFilmActivity.class);
				intentResume.putExtra(DetailFilmActivity.KEY_TITLE,
						((Movie) mAdapterResult.getItem(pos)).title);
				intentResume.putExtra(DetailFilmActivity.KEY_IMDB_ID,
						((Movie) mAdapterResult.getItem(pos)).imdb_id);
				intentResume.putExtra(DetailFilmActivity.KEY_RESUME,
						((Movie) mAdapterResult.getItem(pos)).plot);
				if (((Movie) mAdapterResult.getItem(pos)).poster != null) {
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

		mParamSearch = mEditTextSearch.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mParamSearch)) {
			focusView = mEditTextSearch;
			cancel = true;
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

				for (Movie movie : result) {
					Logger.d("resume: " + movie.imdb_id);
				}
				mProgressSearch.cancel();
				mAdapterResult = new ResultAdapter(result, MainActivity.this);
				mListResult.setAdapter(mAdapterResult);
			}

			@Override
			public void failure(RetrofitError error) {
				mProgressSearch.cancel();
				Toast.makeText(
						MainActivity.this,
						getString(R.string.search_not_found) + " \""
								+ mParamSearch + "\"", Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private void progressDialog(String message) {
		mProgressSearch = new ProgressDialog(this, R.style.ImdbProgressDialog);
		mProgressSearch.setMessage(message);
		mProgressSearch.setCancelable(false);
		mProgressSearch.setIndeterminate(true);
		mProgressSearch.show();
	}
}

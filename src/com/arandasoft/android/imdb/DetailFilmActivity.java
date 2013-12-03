package com.arandasoft.android.imdb;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.arandasoft.android.imdb.app.ImdbApp;
import com.squareup.picasso.Picasso;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>
 * Vista detalle en la que el usuario puede apreciar la informacion o resumen de
 * su pelicula o serie escogida
 * </p>
 * 
 * @author Felipe Calderon <felipeskabarragan@gmail.com>
 * */
public class DetailFilmActivity extends SherlockFragmentActivity {

	/* claves para los valores asociados al intent que despliega esta actividad */
	public static String KEY_TITLE = "title";
	public static String KEY_IMDB_ID = "id";
	public static String KEY_RESUME = "plot";
	public static String KEY_URL_POSTER = "poster";

	/* Componentes de tipo UI de la interfaz grafica */
	private TextView mSummary;
	private ImageView mCover;

	private String mResume;
	private String mUrlCover;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setLogo(R.drawable.logo);

		setContentView(R.layout.activity_detail_film);
		mSummary = (TextView) findViewById(R.id.summary);
		mCover = (ImageView) findViewById(R.id.screenCap);

		/* validamos la data que llega a traves del intent y su consistencia */
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mResume = b.getString(KEY_RESUME);
			getSupportActionBar().setTitle(b.getString(KEY_TITLE));
			mUrlCover = b.getString(KEY_URL_POSTER);
			getImageCover();
		}

		mSummary.setText(mResume);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.detail_film, menu);
		return true;
	}

	/**
	 * metodo el cual nos permite obtener la imagen, bien sea del cache o de la
	 * red, a traves de {@link Picasso}
	 * */
	private void getImageCover() {
		((ImdbApp) getApplication()).getPicasso().load(mUrlCover).into(mCover);
	}
}

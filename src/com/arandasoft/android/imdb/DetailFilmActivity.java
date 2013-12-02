package com.arandasoft.android.imdb;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.arandasoft.android.imdb.app.ImdbApp;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailFilmActivity extends SherlockFragmentActivity {

	public static String KEY_TITLE = "title";
	public static String KEY_IMDB_ID = "id";
	public static String KEY_RESUME = "plot";
	public static String KEY_URL_POSTER = "poster";

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

	private void getImageCover() {
		((ImdbApp) getApplication()).getPicasso().load(mUrlCover).into(mCover);
	}
}

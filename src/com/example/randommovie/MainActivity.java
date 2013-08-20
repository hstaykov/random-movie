package com.example.randommovie;

import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	String movieUrl = "http://www.imdb.com/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TextView txtBox = (TextView) findViewById(R.id.textView1);
		final ImageView imgMovie = (ImageView) findViewById(R.id.imageView1);

		txtBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openImdbPage(movieUrl);
			}
		});

		Button btnHardMovie = (Button) findViewById(R.id.btnHardMove);
		btnHardMovie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findMovie(txtBox, imgMovie, true);
			}
		});

		Button btnEasyMovie = (Button) findViewById(R.id.btnEasyMovie);
		btnEasyMovie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findMovie(txtBox, imgMovie, false);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			trimCache(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		Log.d("DEL DIR", "Deleting the directory on exit..");
		return dir.delete();
	}

	private void findMovie(final TextView txtBox, final ImageView imgMovie,
			Boolean isHard) {
		try {

			ImdbMovie ourMovie = new GetImdbData().execute(isHard).get();
			txtBox.setText(ourMovie.title + "\n" + "(click for more info..)");
			movieUrl = ourMovie.url;
			if (ourMovie.posterUrl.contains("jpg")) {
				new GetImdbImage(imgMovie).execute(ourMovie.posterUrl);
			} else {
				Resources res = getResources();
				imgMovie.setImageDrawable(res
						.getDrawable(R.drawable.ic_launcher));
			}

		} catch (Exception e) {
			Log.e("Main Activity Error", e.toString());
		}
	}

	private void openImdbPage(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true; 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.btn_about){
			startActivity(new Intent(MainActivity.this, AboutActivity.class));
		}
		return true;
	}
}

class GetImdbImage extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;

	public GetImdbImage(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
	}
}

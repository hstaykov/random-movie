package com.example.randommovie;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class ImdbMovie {
	public String title;
	public String url;
	public String posterUrl;

	public ImdbMovie(String initTitle, String initUrl, String initPosterUrl) {
		this.title = initTitle;
		this.url = initUrl;
		this.posterUrl = initPosterUrl;
	}

	public void setTitle(String newTitle) {
		title = newTitle;
	}

	public void setUrl(String newUrl) {
		url = newUrl;
	}

	public void setpPosterUrl(String newPosterUrl) {
		posterUrl = newPosterUrl;
	}

}

class GetImdbData extends AsyncTask<Boolean, Void, ImdbMovie> {

	@Override
	public ImdbMovie doInBackground(Boolean... params) {
		return returnRandomMovie(params[0]);
	}

	private static ImdbMovie returnRandomMovie(Boolean isHard) {
		String movieTitle = "Default title";
		String url = "Default Url";
		String posterUrl = "http://www.barbedwirecity.com/wp-content/uploads/2013/03/imdb_logo.jpg";
		Document doc;
		try {
			if (isHard) {
				int min = 100;
				int max = 1535108;

				Random r = new Random();
				int movieNumber = r.nextInt(max - min + 1) + min;
				doc = Jsoup.connect(
						"http://www.imdb.com/title/tt" + movieNumber + "/")
						.get();

			} else {
				doc = Jsoup.connect("http://www.imdb.com/random/title/").get();
			}
			Elements linkUrl = doc.select("link[href]");
			url = linkUrl.get(0).attr("href");
			
			String title = ((org.jsoup.nodes.Document) doc).title();
			movieTitle = title;
			
			Elements linkPosterUrl = doc.select("img[src]");
			posterUrl = linkPosterUrl.get(1).attr("src");
			
		} catch (IOException e) {
			Log.e("JSOUP ERROR", e.getMessage());
		}
		return new ImdbMovie(movieTitle, url, posterUrl);
	}

}
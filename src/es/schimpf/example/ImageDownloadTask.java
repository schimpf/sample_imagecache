package es.schimpf.example;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask to download an image.
 */
public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

	/** Store to cache the bitmap */
	private CachedImage ci;

	/** Listener to nitify when bitmap has been downloaded. */
	private OnBitmapReadyListener listener;

	/**
	 * Constructor.
	 * 
	 * @param imageView
	 *            The ImageView which will receive the image.
	 */
	public ImageDownloadTask(CachedImage ci, OnBitmapReadyListener listener) {
		this.listener = listener;
		this.ci = ci;
	}

	/**
	 * This function will be executed to download the image in a background
	 * process.
	 * 
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		Log.d("ImageCache", "downloading" + ci.getUrl());
		try {
			InputStream in = new java.net.URL(ci.getUrl()).openStream();
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			return bitmap;
		} catch (Exception e) {
			Log.e("ImageDownload", e.getMessage());
		}
		return null;
	}

	/**
	 * This function will be called after the image download and attaches the
	 * bitmap to the ImageView.
	 * 
	 */
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}
		Log.d("ImageCache", "downloading" + ci.getUrl() + " DONE");

		if (ci != null) {
			ci.setBitmap(bitmap);
			BitmapCache.getInstance().cacheFile(ci);
		}

		if (listener != null) {
			listener.onBitmapReady(bitmap);
		}
	}

}

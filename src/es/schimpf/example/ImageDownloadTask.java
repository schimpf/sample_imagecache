package es.schimpf.example;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 */
public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

	/** Reference to the view which should receive the image */
	private final WeakReference<ImageView> imageRef;

	/** Store to cache the bitmap */
	private CachedImage ci;

	/**
	 * Constructor.
	 * 
	 * @param imageView
	 *            The ImageView which will receive the image.
	 */
	public ImageDownloadTask(CachedImage ci, ImageView imageView) {
		imageRef = new WeakReference<ImageView>(imageView);
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

		if (imageRef != null) {
			ImageView imageView = imageRef.get();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}

		if (ci != null) {
			ci.setBitmap(bitmap);
			ImageCacher.getInstance().cacheFile(ci);
		}

		// data.setBitmap(bitmap);
	}

}

package es.schimpf.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Singleton which provides a function to either load a bitmap from file if
 * available or download and save it if not.
 * 
 * Caching time set to 60 seconds.
 * 
 * @author gschimpf
 * 
 */
public class BitmapCache {

	/** Time the image will be cached in seconds. */
	public static final int CACHE_TIME = 60;

	/** Singleton pattern implementation. */
	private static BitmapCache singleton;

	/** Point to the directory where the cached files are stored. */
	private File cacheDir;

	/**
	 * Initializes the singleton instance
	 * 
	 * @param c
	 *            Context to get the cacheDirectory from.
	 * @return
	 */
	public static BitmapCache initInstance(Context c) {
		singleton = new BitmapCache(c);
		return singleton;
	}

	/**
	 * Singleton pattern implementation.
	 * 
	 * @return
	 */
	public static BitmapCache getInstance() {
		return singleton;
	}

	/**
	 * Singelton pattern implementation.
	 * 
	 * @param context
	 *            Context to get the cacheDirectory from.
	 */
	private BitmapCache(Context context) {
		cacheDir = context.getCacheDir();
	}

	/**
	 * Function to load or downlad an image.
	 * 
	 * @param url
	 *            The URL to download the image from if its not found in cache.
	 * 
	 * @param listener
	 *            Listener which will be notified when the bitmap is ready.
	 */
	public void getImageForView(String url, OnBitmapReadyListener listener) {
		Log.d("ImageCache", "getImageForView [" + url + "]");
		// check cache
		CachedImage ci = new CachedImage();
		ci.setUrl(url);
		if (readCachedFile(ci)) {
			// Hit
			Log.d("ImageCache", "hit");
			listener.onBitmapReady(ci.getBitmap());
		} else {
			// Fail
			Log.d("ImageCache", "fail");
			new ImageDownloadTask(ci, listener).execute();
		}
	}

	protected void cacheFile(CachedImage ci) {
		String name = ci.getKey();
		if (name == null || "".equals(name)) {
			Log.e("ImageCache", "filename is null or empty");
			return;
		}

		File imageFile = new File(cacheDir, name);

		FileOutputStream out;
		try {
			out = new FileOutputStream(imageFile);
			ci.getBitmap().compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (FileNotFoundException e) {
			Log.e("ImageCache", "Exception", e);
		}
	}

	protected boolean readCachedFile(CachedImage ci) {
		String name = ci.getKey();
		if (name == null || "".equals(name)) {
			Log.e("ImageCache", "filename is null or empty");
			return false;
		}
		Log.d("ImageCache", "checking for cachefile [" + name + "]");

		File imageFile = new File(cacheDir, name);
		if (!imageFile.exists()) {
			return false;
		}
		Log.d("ImageCache", "cachefile [" + name + "] exists");

		// TODO datetime
		if (System.currentTimeMillis() - imageFile.lastModified() > CACHE_TIME * 1000) {
			Log.d("ImageCache", "cachefile [" + name + "] out of date");
			return false;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
				options);
		ci.setBitmap(bitmap);
		return true;
	}

	/**
	 * Reads the directory with the cache files and outputs their names to the
	 * log.
	 */
	public void readDir() {
		Log.d("ImageCache", "ImageCacher::readDir()");
		File[] files = cacheDir.listFiles();
		for (File inFile : files) {
			Log.d("ImageCache", "cached file: " + inFile.getName());
		}
	}

	/**
	 * Function to remove all cached files.
	 */
	public void clearCache() {
		Log.d("ImageCache", "ImageCacher::clearCache() Clearing cache");
		File[] files = cacheDir.listFiles();
		for (File inFile : files) {
			inFile.delete();
		}
		Log.d("ImageCache", "ImageCacher::clearCache() All cache files deleted");
	}

}

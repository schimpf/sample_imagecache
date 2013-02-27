package es.schimpf.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageCacher {

	/** Time the image will be cached in seconds. */
	public static final int CACHE_TIME = 60;
	
	private static ImageCacher singleton;

	private File cacheDir;

	public static ImageCacher initInstance(Context c) {
		singleton = new ImageCacher(c);
		return singleton;
	}

	public static ImageCacher getInstance() {
		return singleton;
	}

	private ImageCacher(Context context) {
		cacheDir = context.getCacheDir();
	}

	public void getImageForView(String url, ImageView view) {
		Log.d("ImageCache", "getImageForView [" + url + "]");
		// check cache
		CachedImage ci = new CachedImage();
		ci.setUrl(url);
		if (readCachedFile(ci)) {
			// Hit
			Log.d("ImageCache", "hit");
			view.setImageBitmap(ci.getBitmap());
		} else {
			// Fail
			Log.d("ImageCache", "fail");
			new ImageDownloadTask(ci, view).execute();
		}
	}

	public void cacheFile(CachedImage ci) {
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

	public boolean readCachedFile(CachedImage ci) {
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
		if (System.currentTimeMillis() - imageFile.lastModified() > CACHE_TIME*1000) {
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

	public void readDir() {
		Log.d("ImageCache", "ImageCacher::readDir()");
		File[] files = cacheDir.listFiles();
		for (File inFile : files) {
			Log.d("ImageCache", "cached file: " + inFile.getName());
		}
	}

	/**
	 * Remove all files from cache.
	 * 
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

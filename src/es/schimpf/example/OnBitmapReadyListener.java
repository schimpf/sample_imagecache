package es.schimpf.example;

import android.graphics.Bitmap;

/**
 * Interface to notifiy when a bitmap is ready to use.
 * 
 * @author gschimpf
 *
 */
public interface OnBitmapReadyListener {

	public void onBitmapReady(Bitmap bitmap);
}

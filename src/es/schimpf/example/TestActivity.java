package es.schimpf.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import es.schimpf.example.imagechache.R;

/**
 * TestActivity for the BitmapCache class.
 * 
 * You can find more information about this code in my <a
 * href="http://schimpf.es">blog</a>.
 * 
 * @see <a href="http://schimpf.es">Blog</a>
 * @author Gerrit Schimpf *
 */
public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);

		final BitmapCache ic = BitmapCache.initInstance(this);
		ic.readDir();

		final ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		final ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
		final ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

		Button buttonLoad = (Button) findViewById(R.id.button1);
		Button buttonClear = (Button) findViewById(R.id.button2);

		Button buttonReset = (Button) findViewById(R.id.button3);

		buttonLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ic.getImageForView(
						"http://cdn1.iconfinder.com/data/icons/gestureworks_gesture_glyphs/128/stroke_number_one_gestureworks.png",
						new OnBitmapReadyListener() {
							@Override
							public void onBitmapReady(Bitmap bitmap) {
								imageView1.setImageBitmap(bitmap);
							}
						});

				ic.getImageForView(
						"http://cdn1.iconfinder.com/data/icons/gestureworks_gesture_glyphs/128/stroke_number_two_gestureworks.png",
						new OnBitmapReadyListener() {
							@Override
							public void onBitmapReady(Bitmap bitmap) {
								imageView2.setImageBitmap(bitmap);
							}
						});

				ic.getImageForView(
						"http://cdn1.iconfinder.com/data/icons/gestureworks_gesture_glyphs/128/stroke_number_three_gestureworks.png",
						new OnBitmapReadyListener() {
							@Override
							public void onBitmapReady(Bitmap bitmap) {
								imageView3.setImageBitmap(bitmap);
							}
						});

			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ic.clearCache();
			}
		});

		buttonReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView1.setImageResource(R.drawable.ic_launcher);
				imageView2.setImageResource(R.drawable.ic_launcher);
				imageView3.setImageResource(R.drawable.ic_launcher);
			}
		});

	}

}

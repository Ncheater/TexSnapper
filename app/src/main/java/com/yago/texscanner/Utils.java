package com.yago.texscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
	public static final int READ_WRITE = 1;
	public static final int CAMERA = 2;
	public static final int GALLERY = 3;
	public static final int CROPPER = 4;
	public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public static int sum(int[] values) {
		int sum = 0;
		for (int value : values) {
			sum += value;
		}
		return sum;
	}

	public static void doCrop(Activity activity, Uri picUri, Uri uri) {
		Intent crop = new Intent("com.android.camera.action.CROP");
		crop.setDataAndType(picUri, "image/*")
				.putExtra("crop", true)
				.putExtra("aspectX", 1)
				.putExtra("aspectY", 1)
				.putExtra("outputX", 512)
				.putExtra("outputY", 512)
				.putExtra("return-data", false)
				.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(crop, CROPPER);
	}

	public static Bitmap toGrayscale(Bitmap bmp, boolean inverted) {
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);

		Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

		Canvas canvas = new Canvas(ret);

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bmp, 0, 0, paint);

		if (inverted) return invert(ret);
		else return ret;
	}

	private static Bitmap invert(Bitmap bmp) {
		ColorMatrix cm = new ColorMatrix(new float[]
				{
						-1, 0, 0, 0, 255,
						0, -1, 0, 0, 255,
						0, 0, -1, 0, 255,
						0, 0, 0, 1, 0
				});

		Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

		Canvas canvas = new Canvas(ret);

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bmp, 0, 0, paint);

		return ret;
	}

	public static Bitmap rescale(Bitmap bmp, float contrast, int brightness) {
		ColorMatrix cm = new ColorMatrix(new float[]
				{
						contrast, 0, 0, 0, brightness,
						0, contrast, 0, 0, brightness,
						0, 0, contrast, 0, brightness,
						0, 0, 0, 1, 0
				});

		Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

		Canvas canvas = new Canvas(ret);

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bmp, 0, 0, paint);

		return ret;
	}

	public static double toRGB(double pX) {
		return (pX + 1.0) * (255.0 / 2.0);
	}
}

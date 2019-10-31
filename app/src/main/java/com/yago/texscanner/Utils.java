package com.yago.texscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;

public class Utils {
	public static final int READ_WRITE = 1;
	public static final int CAMERA = 2;
	public static final int GALLERY = 3;
	public static final int CROPPER = 4;

	public static int sum(int[] values) {
		int sum = 0;
		for (int value : values) {
			sum += value;
		}
		return sum;
	}

	private static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}

	public static int adjustBrightness(int color, float brightness) {
		int r = Math.round(Utils.clamp(Color.red(color) * brightness, 0, 255));
		int g = Math.round(Utils.clamp(Color.green(color) * brightness, 0, 255));
		int b = Math.round(Utils.clamp(Color.blue(color) * brightness, 0, 255));

		return Color.rgb(r, g, b);
	}

	public static void doCrop(Activity activity, Uri picUri) {
		Intent crop = new Intent("com.android.camera.action.CROP");
		crop.setDataAndType(picUri, "image/*");
		crop.putExtra("crop", true);
		crop.putExtra("aspectX", 1);
		crop.putExtra("aspectY", 1);
		crop.putExtra("return-data", true);
		activity.startActivityForResult(crop, CROPPER);
	}

	public static int average(int... values) {
		int sum = 0;
		for (int v : values) {
			sum += v;
		}
		return Math.round((float) sum / values.length);
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

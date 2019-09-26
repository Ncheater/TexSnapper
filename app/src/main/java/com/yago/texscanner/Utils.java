package com.yago.texscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import com.google.ar.sceneform.math.Vector3;
import com.vansuita.gaussianblur.GaussianBlur;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
	public static final int CAMERA = 1;
	public static final int GALLERY = 2;
	public static final int CROPPER = 3;
	private static final ExecutorService renderThread = Executors.newSingleThreadExecutor();

	public static void run(Runnable task) {
		renderThread.execute(task);
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

	public static float[][] toHSV(int[] rgb) {
		float[][] hsv = new float[3][rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			Color.colorToHSV(rgb[i], hsv[i]);
		}
		return hsv;
	}

	public static int[] toRGB(float[][] hsv) {
		int[] rgb = new int[hsv.length];
		for (int i = 0; i < hsv.length; i++) {
			rgb[i] = Color.HSVToColor(hsv[i]);
		}
		return rgb;
	}

	public static Bitmap toGrayscale(Bitmap map) {
		Bitmap out = Bitmap.createBitmap(map.getWidth(), map.getHeight(), map.getConfig());
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				int rgb = out.getPixel(x, y);
				int r = Color.red(rgb);
				int g = Color.green(rgb);
				int b = Color.blue(rgb);

				int avg = average(r, g, b);

				out.setPixel(x, y, Color.rgb(avg, avg, avg));
			}
		}

		return out;
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

	public static Bitmap makeNormalMap(Context context, Bitmap source, int contrast, int brightness, int smoothness, int power, boolean invert) {
		Bitmap input = rescale(toGrayscale(source), contrast / 100f, brightness - 255);
		Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());

		input = GaussianBlur.with(context).radius(smoothness).render(input);
		for (int y = 1; y < input.getHeight() - 1; y++) {
			for (int x = 1; x < input.getWidth() - 1; x++) {
				final double tl = Color.red(input.getPixel(x - 1, y + 1));
				final double t = Color.red(input.getPixel(x, y + 1));
				final double tr = Color.red(input.getPixel(x + 1, y + 1));

				final double r = Color.red(input.getPixel(x - 1, y));
				final double l = Color.red(input.getPixel(x + 1, y));

				final double bl = Color.red(input.getPixel(x - 1, y - 1));
				final double b = Color.red(input.getPixel(x, y - 1));
				final double br = Color.red(input.getPixel(x + 1, y - 1));

				double xNormal = tl - tr + 2f * r - 2f * l + bl - br;
				double yNormal = tl - bl + 2f * t - 2f * b + tr - br;

				Vector3 X = new Vector3(1, 0, (float) (power * (invert ? -xNormal : xNormal)));
				Vector3 Y = new Vector3(0, 1, (float) (power * (invert ? -yNormal : yNormal)));

				Vector3 normal = Vector3.cross(X, Y);
				normal = normal.normalized();

				int rgb = Color.rgb((int) Math.round(toRGB(normal.x)), (int) Math.round(toRGB(normal.y)), (int) Math.round(toRGB(normal.z)));

				output.setPixel(x, y, rgb);

				if (x == 1 && y == 1) {
					output.setPixel(x - 1, y - 1, rgb);
				}
				if (x == 1 && y == input.getHeight() - 2) {
					output.setPixel(x - 1, y + 1, rgb);
				}
				if (x == input.getWidth() - 2 && y == 1) {
					output.setPixel(x + 1, y - 1, rgb);
				}
				if (x == input.getWidth() - 2 && y == input.getHeight() - 2) {
					output.setPixel(x + 1, y + 1, rgb);
				}
				if (x == 1) {
					output.setPixel(x - 1, y, rgb);
				}
				if (y == 1) {
					output.setPixel(x, y - 1, rgb);
				}
				if (x == input.getWidth() - 2) {
					output.setPixel(x + 1, y, rgb);
				}
				if (y == input.getHeight() - 2) {
					output.setPixel(x, y + 1, rgb);
				}
			}
		}

		return GaussianBlur.with(context).radius(smoothness).render(output);
	}

	private static double toRGB(double pX) {
		return (pX + 1.0) * (255.0 / 2.0);
	}
}

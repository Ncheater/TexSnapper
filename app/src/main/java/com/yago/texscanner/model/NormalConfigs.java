package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.ar.sceneform.math.Vector3;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.Utils;

public class NormalConfigs extends MapConfig {
	private int contrast = 1000;
	private int brightness = 256;
	private int strength = 0;
	private boolean inverted = false;

	public NormalConfigs(GlobalContext context) {
		super(context);
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public Bitmap render(Bitmap base) {
		final Bitmap map = Utils.rescale(base, contrast / 1000f, brightness - 256);
		final int[] pixels = new int[map.getWidth() * map.getHeight()];
		map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		return makeNormalMap(map, strength, inverted);
	}

	private Bitmap makeNormalMap(Bitmap input, int power, boolean invert) {
		Bitmap output = Bitmap.createBitmap(input);

		for (int y = 1; y < input.getHeight() - 1; y++) {
			for (int x = 1; x < input.getWidth() - 1; x++) {
				double tl = Color.red(input.getPixel(x - 1, y + 1));
				double t = Color.red(input.getPixel(x, y + 1));
				double tr = Color.red(input.getPixel(x + 1, y + 1));

				double r = Color.red(input.getPixel(x - 1, y));
				double l = Color.red(input.getPixel(x + 1, y));

				double bl = Color.red(input.getPixel(x - 1, y - 1));
				double b = Color.red(input.getPixel(x, y - 1));
				double br = Color.red(input.getPixel(x + 1, y - 1));

				double xNormal = tl - tr + 2f * r - 2f * l + bl - br;
				double yNormal = tl - bl + 2f * t - 2f * b + tr - br;

				Vector3 X = new Vector3(1, 0, (float) ((power + 1) / 1000f * (invert ? xNormal : -xNormal)));
				Vector3 Y = new Vector3(0, 1, (float) ((power + 1) / 1000f * (invert ? yNormal : -yNormal)));

				Vector3 normal = Vector3.cross(X, Y);
				normal = normal.normalized();

				@SuppressWarnings("SuspiciousNameCombination")
				int rgb = Color.rgb((int) Math.round(Utils.toRGB(normal.x)), (int) Math.round(Utils.toRGB(normal.y)), (int) Math.round(Utils.toRGB(normal.z)));

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

		return output;
	}
}

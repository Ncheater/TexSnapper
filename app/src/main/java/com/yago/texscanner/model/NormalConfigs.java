package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.ar.sceneform.math.Vector3;
import com.vansuita.gaussianblur.GaussianBlur;
import com.yago.texscanner.Utils;

public class NormalConfigs extends MapConfig {
	private int small = 1000;
	private int big = 256;
	private int strength = 0;
	private int smoothness = 15;
	private boolean inverted = false;

	public NormalConfigs(Bitmap map) {
		super(map);
	}

	public void setSmall(int small) {
		this.small = small;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public void setSmoothness(int smoothness) {
		this.smoothness = smoothness;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public Bitmap render(Bitmap base) {
		final Bitmap map = Utils.rescale(base, small / 1000f, big - 256);
		final int[] pixels = new int[map.getWidth() * map.getHeight()];
		map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		return makeNormalMap(map, smoothness, strength, inverted);
	}

	private static Bitmap makeNormalMap(Bitmap input, int smoothness, int power, boolean invert) {
		Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());

		input = GaussianBlur.with(null).radius(smoothness).render(input);
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

		return GaussianBlur.with(null).radius(smoothness).render(output);
	}
}

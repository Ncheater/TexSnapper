package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.support.v4.graphics.ColorUtils;
import com.yago.texscanner.Utils;

public class RoughnessConfigs extends MapConfig {
	private int contrast = 900;
	private int brightness = 300;
	private int fac = 50;
	private boolean inverted = false;

	public RoughnessConfigs(Bitmap map) {
		super(map);
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public void setFac(int fac) {
		this.fac = fac;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public Bitmap render(Bitmap base) {
			final Bitmap map = Utils.rescale(base, contrast / 1000f, brightness - 256);
			final int[] pixels = new int[map.getWidth() * map.getHeight()];
			map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = adjust(pixels[i], fac);
			}

			map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

			return Utils.toGrayscale(map, inverted);
	}

	private static int adjust(int color, int fac) {
		float[] hsl = new float[3];
		ColorUtils.colorToHSL(color, hsl);

		if (hsl[2] > 0.5f) {
			hsl[2] += (hsl[2] - 0.5f) * ((1 - hsl[2]) * (fac / 25f));
		} else if (hsl[2] < 0.5f) {
			hsl[2] -= (0.5f - hsl[2]) * (hsl[2] * (fac / 25f));
		}

		return ColorUtils.HSLToColor(hsl);
	}
}

package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.support.v4.graphics.ColorUtils;
import com.yago.texscanner.Utils;

public class DiffuseConfigs extends MapConfig {
	private int contrast = 1000;
	private int brightness = 256;
	private int shadow = 0;
	private int light = 0;

	public DiffuseConfigs(Bitmap map) {
		super(map);
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public void setShadow(int shadow) {
		this.shadow = shadow;
	}

	public void setLight(int light) {
		this.light = light;
	}

	@Override
	public Bitmap render(Bitmap base) {
		final Bitmap map = Utils.rescale(base, contrast / 1000f, brightness - 256);
		final int[] pixels = new int[map.getWidth() * map.getHeight()];
		map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = adjust(pixels[i], shadow, light);
		}

		map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		return map;
	}

	private static int adjust(int color, int shadow, int light) {
		float[] hsl = new float[3];
		ColorUtils.colorToHSL(color, hsl);

		if (hsl[2] < 0.5f) {
			hsl[2] += (0.5f - hsl[2]) * ((1 - hsl[2]) * (shadow / 50f));
			hsl[1] -= (shadow / 100f) * hsl[1];
		} else if (hsl[2] > 0.7f) {
			hsl[2] -= (hsl[2] - 0.7f) * (hsl[2] * (light / 50f));
			if (hsl[2] > 0.9f) {
				hsl[1] = 0;
			} else {
				hsl[1] -= (light / 100f) * hsl[1];
			}
		}

		return ColorUtils.HSLToColor(hsl);
	}
}
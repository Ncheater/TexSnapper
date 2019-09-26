package com.yago.texscanner.model;

import android.graphics.Bitmap;
import com.yago.texscanner.Utils;

public class DiffuseConfigs extends MapConfig {
	private int contrast = 1000;
	private int brightness = 256;
	private int shadow = 0;
	private int light = 0;

	public DiffuseConfigs(Bitmap map) {
		super(map);
	}

	public int getContrast() {
		return contrast;
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getShadow() {
		return shadow;
	}

	public void setShadow(int shadow) {
		this.shadow = shadow;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public Bitmap render() {
		final Bitmap map = Utils.rescale(getMap(), contrast / 1000f, brightness - 256);
		final int[] pixels = new int[map.getWidth() * map.getHeight()];
		final int length = pixels.length;
		map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		final float[][] hsv = Utils.toHSV(pixels);
		for (int i = 0; i < length; i++) {
			if (hsv[i][2] < 0.4) {
				hsv[i][2] += (1 - hsv[i][2]) * (shadow / 100f);
			} else if (hsv[i][2] > 0.6) {
				hsv[i][2] -= hsv[i][2] * (light / 100f);
			}
		}

		map.setPixels(Utils.toRGB(hsv), 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		return map;
	}
}

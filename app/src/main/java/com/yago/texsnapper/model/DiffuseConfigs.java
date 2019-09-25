package com.yago.texsnapper.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.yago.texsnapper.Utils;

public class DiffuseConfigs extends MapConfig {
	private int contrast = 0;
	private int brightness = 0;
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
		Bitmap map = Utils.rescale(getMap(), contrast / 1000f, brightness - 256);
		int[] pixels = new int[map.getWidth() * map.getHeight()];
		map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());
		for (int i = 0; i < pixels.length; i++) {
				int r = Color.red(pixels[i]);
				int g = Color.green(pixels[i]);
				int b = Color.blue(pixels[i]);

				int mean = Math.round((r + g + b) / 3f);
				if (mean < 128) {
					pixels[i] = Color.rgb(Math.round(r + 128 * shadow / 100f), Math.round(g + 128 * shadow / 100f), Math.round(b + 128 * shadow / 100f));
				} else {
					pixels[i] = Color.rgb(Math.round(r - 128 * shadow / 100f), Math.round(g - 128 * shadow / 100f), Math.round(b - 128 * shadow / 100f));
			}
		}

		map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		return map;
	}
}

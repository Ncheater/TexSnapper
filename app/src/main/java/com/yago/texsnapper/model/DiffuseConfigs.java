package com.yago.texsnapper.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.yago.texsnapper.Utils;

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
		Bitmap map = Utils.rescale(getMap(), contrast / 1000f, brightness - 256);
		int[] pixels = new int[map.getWidth() * map.getHeight()];
		map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());
		for (int i = 0; i < pixels.length; i++) {
				float[] hsv = new float[3];
				Color.colorToHSV(pixels[i], hsv);

				if (hsv[2] < 0.3) {
					hsv[2] += (1 - hsv[2]) * (shadow / 100f);
					pixels[i] = Color.HSVToColor(hsv);
				} else if (hsv[2] > 0.7){
					hsv[2] -= hsv[2] * (light / 100f);
					pixels[i] = Color.HSVToColor(hsv);
			}
		}

		map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

		return map;
	}
}

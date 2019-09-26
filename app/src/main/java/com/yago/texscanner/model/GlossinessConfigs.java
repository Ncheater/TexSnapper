package com.yago.texscanner.model;

import android.graphics.Bitmap;

public class GlossinessConfigs extends MapConfig {
	private int contrast = 0;
	private int brightness = 0;
	private int smoothness = 0;
	private int fac = 0;
	private boolean inverted = false;

	public GlossinessConfigs(Bitmap map) {
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

	public int getSmoothness() {
		return smoothness;
	}

	public void setSmoothness(int smoothness) {
		this.smoothness = smoothness;
	}

	public int getFac() {
		return fac;
	}

	public void setFac(int fac) {
		this.fac = fac;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}
}

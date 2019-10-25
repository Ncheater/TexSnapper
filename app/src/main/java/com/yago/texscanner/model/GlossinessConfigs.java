package com.yago.texscanner.model;

import android.graphics.*;
import android.support.v4.graphics.ColorUtils;
import com.yago.texscanner.Utils;

public class GlossinessConfigs extends MapConfig {
	private int contrast = 1250;
	private int brightness = 225;
	private int fac = 100;
	private boolean inverted = false;
	private boolean nometal = false;

	public GlossinessConfigs(Bitmap map) {
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

	public void setNoMetal(boolean nometal) {
		this.nometal = nometal;
	}

	@Override
	public Bitmap render(Bitmap base) {
		if (nometal) {
			Bitmap ret = Bitmap.createBitmap(base.getWidth(), base.getHeight(), base.getConfig());

			Canvas canvas = new Canvas(ret);

			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			canvas.drawRect(new Rect(0, 0, ret.getWidth(), ret.getHeight()), paint);

			return ret;
		} else {
			final Bitmap map = Utils.rescale(base, contrast / 1000f, brightness - 256);
			final int[] pixels = new int[map.getWidth() * map.getHeight()];
			map.getPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = adjust(pixels[i], fac);
			}

			map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());

			return Utils.toGrayscale(map, inverted);
		}
	}

	private static int adjust(int color, int fac) {
		float[] hsl = new float[3];
		ColorUtils.colorToHSL(color, hsl);

		if (hsl[2] > 0.7f) {
			hsl[2] += (hsl[2] - 0.7f) * ((1 - hsl[2]) * (fac / 25f));
		} else if (hsl[2] < 0.7f) {
			hsl[2] -= (0.7f - hsl[2]) * (hsl[2] * (fac / 25f));
		}

		return ColorUtils.HSLToColor(hsl);
	}
}

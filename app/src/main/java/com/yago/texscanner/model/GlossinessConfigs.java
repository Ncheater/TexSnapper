package com.yago.texscanner.model;

import android.graphics.*;
import android.support.v4.graphics.ColorUtils;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.Utils;

public class GlossinessConfigs extends MapConfig {
	private int contrast = 1250;
	private int brightness = 225;
	private int fac = 100;
	private boolean inverted = false;
	private boolean nometal = false;

	public GlossinessConfigs(GlobalContext context) {
		super(context);
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
			setBuffer(Utils.rescale(base, contrast / 1000f, brightness - 256));
			final int[] pixels = new int[getBuffer().getWidth() * getBuffer().getHeight()];
			final float[][] hsls = new float[pixels.length][3];
			getBuffer().getPixels(pixels, 0, getBuffer().getWidth(), 0, 0, getBuffer().getWidth(), getBuffer().getHeight());

			for (int i = 0; i < pixels.length; i++) {
				ColorUtils.colorToHSL(pixels[i], hsls[i]);
			}
			for (int i = 0; i < pixels.length; i++) {
				adjust(i, pixels, hsls[i], fac);
			}

			getBuffer().setPixels(pixels, 0, getBuffer().getWidth(), 0, 0, getBuffer().getWidth(), getBuffer().getHeight());

			return Utils.toGrayscale(getBuffer(), inverted);
		}
	}

	private static void adjust(int i, int[] pixels, float[] hsl, int fac) {
		if (hsl[2] > 0.7f) {
			hsl[2] += (hsl[2] - 0.7f) * ((1 - hsl[2]) * (fac / 25f));
		} else if (hsl[2] < 0.7f) {
			hsl[2] -= (0.7f - hsl[2]) * (hsl[2] * (fac / 25f));
		}

		pixels[i] = ColorUtils.HSLToColor(hsl);
	}
}

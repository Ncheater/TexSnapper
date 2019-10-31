package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import com.yago.texscanner.GlobalContext;

public abstract class MapConfig {
	private final GlobalContext context;
	private Bitmap buffer = null;

	MapConfig(GlobalContext context) {
		this.context = context;
	}

	public abstract Bitmap render(@Nullable Bitmap base);

	public Bitmap getMap() {
		return context.getScaledImage();
	}

	public GlobalContext getContext() {
		return context;
	}

	public Bitmap getBuffer() {
		return buffer;
	}

	public void setBuffer(Bitmap img) {
		this.buffer = img;
	}
}

package com.yago.texscanner.model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

public abstract class MapConfig {
	private Bitmap buffer = null;
	private final Bitmap map;

	MapConfig(Bitmap map) {
		this.map = map;
	}

	public Bitmap getMap() {
		return map;
	}

	public abstract Bitmap render(@Nullable Bitmap base);

	public Bitmap getBuffer() {
		return buffer;
	}

	public void setBuffer(Bitmap img) {
		this.buffer = img;
	}
}

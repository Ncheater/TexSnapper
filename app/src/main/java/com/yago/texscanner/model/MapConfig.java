package com.yago.texscanner.model;

import android.graphics.Bitmap;

public class MapConfig {
	private final Bitmap map;

	MapConfig(Bitmap map) {
		this.map = map;
	}

	public Bitmap getMap() {
		return map;
	}
}

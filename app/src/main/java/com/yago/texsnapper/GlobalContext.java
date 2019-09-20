package com.yago.texsnapper;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.HashMap;

public class GlobalContext extends Application {
	private Bitmap sourceImage;
	private final HashMap<MapType, Bitmap> maps = new HashMap<>();

	public void put(MapType type, Bitmap map) {
		maps.put(type, map);
	}

	public Bitmap getMap(MapType type) {
		Bitmap map = maps.get(type);
		if (map == null) return sourceImage;
		else return map;
	}

	public Bitmap getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(Bitmap sourceImage) {
		this.sourceImage = sourceImage;
	}
}

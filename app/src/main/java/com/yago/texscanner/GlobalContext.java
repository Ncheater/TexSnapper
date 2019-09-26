package com.yago.texscanner;

import android.app.Application;
import android.graphics.Bitmap;
import com.yago.texscanner.model.*;

import java.util.HashMap;

public class GlobalContext extends Application {
	private Bitmap sourceImage;
	private final HashMap<MapType, MapConfig> maps = new HashMap<>();
	private String currPath;

	public void setSourceImage(Bitmap sourceImage) {
		this.sourceImage = sourceImage;
	}

	public MapConfig getMap(MapType type) {
		switch (type) {
			case DIFFUSE:
				if (maps.get(type) == null) return new DiffuseConfigs(sourceImage);
			case HEIGHT:
				if (maps.get(type) == null) return new HeightConfigs(sourceImage);
			case ROUGHNESS:
				if (maps.get(type) == null) return new RoughnessConfigs(sourceImage);
			case GLOSSINESS:
				if (maps.get(type) == null) return new GlossinessConfigs(sourceImage);
			case NORMAL:
				if (maps.get(type) == null) return new NormalConfigs(sourceImage);
		}
		return maps.get(type);
	}

	public String getCurrPath() {
		return currPath == null ? "" : currPath;
	}

	public void setCurrPath(String currPath) {
		this.currPath = currPath;
	}
}

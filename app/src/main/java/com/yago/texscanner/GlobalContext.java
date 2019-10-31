package com.yago.texscanner;

import android.app.Application;
import android.graphics.Bitmap;
import com.yago.texscanner.model.*;

import java.util.HashMap;

public class GlobalContext extends Application {
	private Bitmap sourceImage;
	private HashMap<MapType, MapConfig> maps = new HashMap<>();
	private String currPath;

	public void setSourceImage(Bitmap sourceImage) {
		this.sourceImage = sourceImage;
	}

	public Bitmap getSourceImage() {
		return sourceImage;
	}

	public Bitmap getScaledImage() {
		return Bitmap.createScaledBitmap(sourceImage, 256, 256, false);
	}

	public MapConfig getMap(MapType type) {
		switch (type) {
			case DIFFUSE:
				if (maps.get(type) == null) maps.put(type, new DiffuseConfigs(this));
				break;
			case HEIGHT:
				if (maps.get(type) == null) maps.put(type, new HeightConfigs(this));
				break;
			case ROUGHNESS:
				if (maps.get(type) == null) maps.put(type, new RoughnessConfigs(this));
				break;
			case GLOSSINESS:
				if (maps.get(type) == null) maps.put(type, new GlossinessConfigs(this));
				break;
			case NORMAL:
				if (maps.get(type) == null) maps.put(type, new NormalConfigs(this));
				break;
		}
		return maps.get(type);
	}

	public String getCurrPath() {
		return currPath == null ? "" : currPath;
	}

	public void setCurrPath(String currPath) {
		this.currPath = currPath;
	}
	public void release() {
		sourceImage = null;
		maps = new HashMap<>();
		currPath = null;
	}
}

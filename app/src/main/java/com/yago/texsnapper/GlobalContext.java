package com.yago.texsnapper;

import android.app.Application;
import android.graphics.Bitmap;

public class GlobalContext extends Application {
	private Bitmap sourceImage;

	public Bitmap getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(Bitmap sourceImage) {
		this.sourceImage = sourceImage;
	}
}

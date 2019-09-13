package com.yago.texsnapper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utils {
	public static final int CAMERA = 1;
	public static final int GALLERY = 2;

	private static void doCrop(Activity activity, Uri picUri) {
		try {
			Intent crop = new Intent("com.android.camera.action.CROP");
			crop.setDataAndType(picUri, "image/*");
			crop.putExtra("crop", true);
			crop.putExtra("aspectX", 1);
			crop.putExtra("aspectY", 1);
			crop.putExtra("return-data", true);
			activity.startActivityForResult(crop, 2);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "Seu dispositivo não suporta recortar imagens, a imagem será ajustada a partir do centro.", Toast.LENGTH_LONG).show();
		}
	}
}

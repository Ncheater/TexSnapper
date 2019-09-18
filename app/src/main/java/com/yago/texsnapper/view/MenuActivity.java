package com.yago.texsnapper.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.yago.texsnapper.BuildConfig;
import com.yago.texsnapper.GlobalContext;
import com.yago.texsnapper.R;
import com.yago.texsnapper.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

	private GlobalContext global;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		ImageButton camera = findViewById(R.id.camera_btn);
		ImageButton gallery = findViewById(R.id.gallery_btn);

		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);

		global = (GlobalContext) getApplication();
	}

	@Override
	public void onClick(View view) {
		ImageButton clk = (ImageButton) view;

		switch (clk.getId()) {
			case R.id.camera_btn:
				boolean CAMERA = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
				if (!CAMERA) ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
				startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), Utils.CAMERA);
				break;
			case R.id.gallery_btn:
				Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				gallery.setType("image/*");
				gallery.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Utils.GALLERY);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		switch (requestCode) {
			case Utils.CAMERA:
				if (resultCode == Activity.RESULT_OK) {
					assert data != null;
					assert data.getExtras() != null;
					Bitmap img = (Bitmap) data.getExtras().get("data");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					assert img != null;
					img.compress(Bitmap.CompressFormat.PNG, 100, baos);
					String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), img, "", null);

					Utils.doCrop(this, Uri.parse(path));
				}
				break;
			case Utils.GALLERY:
				if (resultCode == Activity.RESULT_OK) {
					assert data != null;
					Uri uri = data.getData();

					assert uri != null;
					Utils.doCrop(this, uri);
				}
				break;
			case Utils.CROPPER:
				if (resultCode == Activity.RESULT_OK) {
					assert data != null;
					assert data.getExtras() != null;
					Bitmap img = (Bitmap) data.getExtras().get("data");
					global.setSourceImage(img);
					Toast.makeText(this, "Imagem cortada com sucesso!", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	public String getFullPath(Uri uri) {

		String path;
		String[] projection = { MediaStore.Files.FileColumns.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

		if(cursor == null){
			path = uri.getPath();
		}
		else{
			cursor.moveToFirst();
			int column_index = cursor.getColumnIndexOrThrow(projection[0]);
			path = cursor.getString(column_index);
			cursor.close();
		}

		return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
	}
}

package com.yago.texscanner.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.R;
import com.yago.texscanner.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

	private GlobalContext global;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		ImageButton camera = findViewById(R.id.camera_btn);
		ImageButton gallery = findViewById(R.id.gallery_btn);

		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);

		global = (GlobalContext) getApplication();
	}

	@Override
	public void onClick(View view) {
		ImageButton clk = (ImageButton) view;
		vibrator.vibrate(15);

		switch (clk.getId()) {
			case R.id.camera_btn:
				if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
				if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
					File img = makeTempFile();
					assert img != null;
					startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE").putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.yago.texscanner", img)), Utils.CAMERA);
					break;
				} else {
					Toast.makeText(this, "É necessário permitir o acesso à camera para utilizar esta função", Toast.LENGTH_LONG).show();
					break;
				}
			case R.id.gallery_btn:
				startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Utils.GALLERY);
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == Utils.CAMERA) {
			if (Utils.sum(grantResults) == 0) {
				File img = makeTempFile();
				assert img != null;
				startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE").putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.yago.texscanner", img)), Utils.CAMERA);
			} else {
				Toast.makeText(this, "É necessário permitir o acesso à camera para utilizar esta função", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		switch (requestCode) {
			case Utils.CAMERA:
				if (resultCode == Activity.RESULT_OK) {
					File file = new File(global.getCurrPath());
					Utils.doCrop(this, Uri.fromFile(file));
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
					Bitmap src = (Bitmap) data.getExtras().get("data");
					assert src != null;
					Bitmap img = Bitmap.createBitmap(src, 0, 0, src.getWidth() - (src.getWidth() % 4), src.getHeight() - (src.getHeight() % 4));
					global.setSourceImage(img);
					Toast.makeText(this, "Imagem cortada com sucesso!", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(MenuActivity.this, MappingActivity.class));
				}
				break;
		}
	}

	private File makeTempFile() {
		try {
			String filename = "SNAP_" + new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
			File file = File.createTempFile(filename, ".png", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
			global.setCurrPath(file.getAbsolutePath());
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

package com.yago.texsnapper.view;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.yago.texsnapper.R;
import com.yago.texsnapper.Utils;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		ImageButton camera = findViewById(R.id.camera_btn);
		ImageButton gallery = findViewById(R.id.gallery_btn);

		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		ImageButton clk = (ImageButton) view;

		switch (clk.getId()) {
			case R.id.camera_btn:
				startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), Utils.CAMERA);
				break;
			case R.id.gallery_btn:
				Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				gallery.setType("image/*");
				gallery.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(gallery, "Selecione uma imagem"), Utils.GALLERY);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		switch (requestCode) {
			case Utils.CAMERA:
				if (resultCode == Activity.RESULT_OK) {

				}
				break;
			case Utils.GALLERY:
				if (resultCode == Activity.RESULT_OK) {

				}
				break;
		}
	}
}

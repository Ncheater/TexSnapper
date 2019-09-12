package com.yago.texsnapper.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.yago.texsnapper.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
	private final boolean CAMERA = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
	private final boolean READ_STORAGE = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	private final boolean WRITE_STORAGE = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	private final List<String> pendingPerms = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (!CAMERA) pendingPerms.add(Manifest.permission.CAMERA);
		if (!READ_STORAGE) pendingPerms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		if (!WRITE_STORAGE) pendingPerms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

		ActivityCompat.requestPermissions(this, pendingPerms.toArray(new String[0]), 1);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 1) {
			if (Arrays.equals(grantResults, new int[]{PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED})) {

			} else {
				finishAffinity();
			}
		}
	}
}

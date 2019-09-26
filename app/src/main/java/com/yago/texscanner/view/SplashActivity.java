package com.yago.texscanner.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.yago.texscanner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
	private final List<String> pendingPerms = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		boolean READ_STORAGE = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
		boolean WRITE_STORAGE = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

		if (!READ_STORAGE) pendingPerms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		if (!WRITE_STORAGE) pendingPerms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (pendingPerms.size() == 0) {
			startActivity(new Intent(SplashActivity.this, MenuActivity.class));
			finish();
		} else {
			ActivityCompat.requestPermissions(this, pendingPerms.toArray(new String[0]), 1);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 1) {
			if (Arrays.equals(grantResults, new int[]{PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED})) {
				startActivity(new Intent(SplashActivity.this, MenuActivity.class));
				finish();
			} else {
				finishAffinity();
			}
		}
	}
}

package com.yago.texscanner.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.yago.texscanner.R;
import com.yago.texscanner.Utils;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
	private final List<String> pendingPerms = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) pendingPerms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) pendingPerms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (pendingPerms.size() == 0) {
			Utils.executor.execute(() -> {
				try {
					Thread.sleep(1500);
					runOnUiThread(() -> {
						startActivity(new Intent(SplashActivity.this, MenuActivity.class));
						finish();
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		} else {
			ActivityCompat.requestPermissions(this, pendingPerms.toArray(new String[0]), Utils.READ_WRITE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == Utils.READ_WRITE) {
			if (Utils.sum(grantResults) == 0) {
				Utils.executor.execute(() -> {
					try {
						Thread.sleep(1500);
						runOnUiThread(() -> {
							startActivity(new Intent(SplashActivity.this, MenuActivity.class));
							finish();
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
			} else {
				Toast.makeText(this, "É necessário permitir o acesso aos arquivos locais para utilizar o aplicativo", Toast.LENGTH_LONG).show();
				finishAffinity();
			}
		}
	}
}

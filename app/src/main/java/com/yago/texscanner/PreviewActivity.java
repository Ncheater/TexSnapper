package com.yago.texscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {

	private GlobalContext global;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		global = (GlobalContext) getApplication();


	}
}

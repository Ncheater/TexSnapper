package com.yago.texscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.PreviewActivity;
import com.yago.texscanner.R;
import com.yago.texscanner.view.fragment.*;

import java.util.ArrayList;
import java.util.List;

public class MappingActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

	private List<Fragment> frags = new ArrayList<Fragment>() {{
		add(new DiffuseFragment());
		add(new HeightFragment());
		add(new RoughnessFragment());
		add(new GlossinessFragment());
		add(new NormalFragment());
	}};
	private final FragmentManager manager = getSupportFragmentManager();
	private GlobalContext global;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapping);
		global = (GlobalContext) getApplication();
		TabLayout tabs = findViewById(R.id.tabLayout);
		FloatingActionButton save = findViewById(R.id.save);
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment, frags.get(0)).commitNow();

		save.setOnClickListener(view -> startActivity(new Intent(MappingActivity.this, PreviewActivity.class)));

		tabs.addOnTabSelectedListener(this);
	}

	@Override
	public void onTabSelected(TabLayout.Tab tab) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment, frags.get(tab.getPosition())).commitNow();
	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) {

	}

	@Override
	public void onTabReselected(TabLayout.Tab tab) {

	}

	@Override
	public void onBackPressed() {
		//TODO Terminar a mensagem de confirmação
		global.release();
		super.onBackPressed();
	}
}

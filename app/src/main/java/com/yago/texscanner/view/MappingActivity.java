package com.yago.texscanner.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.yago.texscanner.R;
import com.yago.texscanner.view.fragment.*;

import java.util.ArrayList;
import java.util.List;

public class MappingActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

	private List<Fragment> frags = new ArrayList<Fragment>(){{
		add(new DiffuseFragment());
		add(new HeightFragment());
		add(new RoughnessFragment());
		add(new GlossinessFragment());
		add(new NormalFragment());
	}};
	private final FragmentManager manager = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapping);
		TabLayout tabs = findViewById(R.id.tabLayout);
		FloatingActionButton save = findViewById(R.id.save);
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment, frags.get(0)).commitNow();

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(MappingActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
			}
		});

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
}

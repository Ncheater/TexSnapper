package com.yago.texsnapper.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.yago.texsnapper.R;
import com.yago.texsnapper.view.fragment.*;

import java.util.ArrayList;
import java.util.List;

public class MappingActivity extends AppCompatActivity {

	private final List<Fragment> frags = new ArrayList<Fragment>(){{
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
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(MappingActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
			}
		});

		tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.fragment, frags.get(tab.getPosition()));
				transaction.commit();

				View holder = findViewById(R.id.fragment);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
	}
}

package com.yago.texscanner.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.MapType;
import com.yago.texscanner.R;
import com.yago.texscanner.view.fragment.*;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MappingActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, DirectoryChooserFragment.OnFragmentInteractionListener {

	private TabLayout tabs;
	private FloatingActionButton save;
	private DirectoryChooserFragment chooser;

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
		final DirectoryChooserConfig config = DirectoryChooserConfig.builder().newDirectoryName("Escolha um local para salvar os mapas").build();
		chooser = DirectoryChooserFragment.newInstance(config);
		global = (GlobalContext) getApplication();
		tabs = findViewById(R.id.tabLayout);
		save = findViewById(R.id.save);
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment, frags.get(0)).commitNow();

		save.hide();
		save.setOnClickListener(view -> chooser.show(getFragmentManager(), null));

		ViewGroup tabGroup = ((ViewGroup) tabs.getChildAt(0));

		tabGroup.getChildAt(2).setClickable(false);
		tabGroup.getChildAt(3).setClickable(false);
		tabGroup.getChildAt(4).setClickable(false);
		tabGroup.getChildAt(2).setAlpha(0.3f);
		tabGroup.getChildAt(3).setAlpha(0.3f);
		tabGroup.getChildAt(4).setAlpha(0.3f);

		tabs.addOnTabSelectedListener(this);
	}

	@Override
	public void onTabSelected(TabLayout.Tab tab) {
		ViewGroup tabGroup = ((ViewGroup) tabs.getChildAt(0));
		switch (tab.getPosition()) {
			case 1:
				tabGroup.getChildAt(2).setClickable(true);
				tabGroup.getChildAt(2).setAlpha(1);
				break;
			case 2:
				tabGroup.getChildAt(3).setClickable(true);
				tabGroup.getChildAt(3).setAlpha(1);
				break;
			case 3:
				tabGroup.getChildAt(4).setClickable(true);
				tabGroup.getChildAt(4).setAlpha(1);
				break;
			case 4:
				save.show();
				break;
		}
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
		new AlertDialog.Builder(this)
				.setTitle("Abortar operação")
				.setMessage("Tem certeza que deseja cancelar a configuração dos mapas?")
				.setNegativeButton("Não", null)
				.setPositiveButton("Sim", (dialogInterface, i) -> {
					global.release();
					super.onBackPressed();
				}).create().show();
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onSelectDirectory(@NonNull String path) {
		final View overlay = findViewById(R.id.saving_overlay);
		final TextView text = findViewById(R.id.saving_text);
		final ProgressBar progress = findViewById(R.id.saving_progress);

		chooser.dismiss();

		overlay.setVisibility(View.VISIBLE);
		findViewById(R.id.main_layout).setEnabled(false);
		Executors.newSingleThreadExecutor().execute(() -> {
			String stamp = "texture_" + System.nanoTime();
			Bitmap height = null;

			File folder = new File(path + "/" + stamp);

			if (!folder.exists()) {
				if (!folder.mkdir()) {
					stamp = "";
				}
			}

			try (FileOutputStream fos = new FileOutputStream(new File(path + "/" + stamp, "mapa_d.png"))) {
				text.post(() -> text.setText("Salvando mapa de difusão"));
				global.getMap(MapType.DIFFUSE).render(global.getSourceImage()).compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				progress.post(() -> progress.incrementProgressBy(1));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (FileOutputStream fos = new FileOutputStream(new File(path + "/" + stamp, "mapa_h.png"))) {
				text.post(() -> text.setText("Salvando mapa de altitude"));
				height = global.getMap(MapType.HEIGHT).render(global.getSourceImage());
				height.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				progress.post(() -> progress.incrementProgressBy(1));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (FileOutputStream fos = new FileOutputStream(new File(path + "/" + stamp, "mapa_r.png"))) {
				text.post(() -> text.setText("Salvando mapa de dureza"));
				global.getMap(MapType.ROUGHNESS).render(global.getSourceImage()).compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				progress.post(() -> progress.incrementProgressBy(1));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (FileOutputStream fos = new FileOutputStream(new File(path + "/" + stamp, "mapa_g.png"))) {
				text.post(() -> text.setText("Salvando mapa de metalicidade"));
				global.getMap(MapType.GLOSSINESS).render(global.getSourceImage()).compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				progress.post(() -> progress.incrementProgressBy(1));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (FileOutputStream fos = new FileOutputStream(new File(path + "/" + stamp, "mapa_n.png"))) {
				text.post(() -> text.setText("Salvando mapa normal"));
				global.getMap(MapType.NORMAL).render(height).compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				progress.post(() -> progress.incrementProgressBy(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
			runOnUiThread(() -> {
				overlay.setVisibility(View.INVISIBLE);
				Toast.makeText(this, "Texturas salvas com sucesso em " + path, Toast.LENGTH_LONG).show();
			});

			finish();
		});
	}

	@Override
	public void onCancelChooser() {
		chooser.dismiss();
	}
}

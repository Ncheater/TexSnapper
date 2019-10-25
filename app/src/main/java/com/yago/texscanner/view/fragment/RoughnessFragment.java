package com.yago.texscanner.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.MapType;
import com.yago.texscanner.R;
import com.yago.texscanner.Utils;
import com.yago.texscanner.model.RoughnessConfigs;

public class RoughnessFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

	private ImageView img;
	private SeekBar contrast, brightness, fac;
	private RoughnessConfigs configs;
	private Bitmap baseBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_roughness, container, false);
		img = v.findViewById(R.id.preview_pic);
		contrast = v.findViewById(R.id.roughness_contrast);
		brightness = v.findViewById(R.id.roughness_bright);
		fac = v.findViewById(R.id.roughness_fac);
		Switch invert = v.findViewById(R.id.roughness_invert);

		contrast.setOnSeekBarChangeListener(this);
		brightness.setOnSeekBarChangeListener(this);
		fac.setOnSeekBarChangeListener(this);
		invert.setOnClickListener(view -> {
			boolean checked = ((Switch) view).isChecked();
			if (checked) {
				configs.setInverted(true);
				refresh();
			} else {
				configs.setInverted(false);
				refresh();
			}
		});

		assert getActivity() != null;
		configs = (RoughnessConfigs) ((GlobalContext) getActivity().getApplication()).getMap(MapType.ROUGHNESS);
		baseBitmap = ((GlobalContext) getActivity().getApplication()).getMap(MapType.DIFFUSE).getBuffer();

		init();
		return v;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (seekBar == contrast) {
			configs.setContrast(i);
		} else if (seekBar == brightness) {
			configs.setBrightness(i);
		} else if (seekBar == fac) {
			configs.setFac(i);
		}
		refresh();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	private void refresh() {
		Utils.run(img, configs, configs.getMap());
	}

	private void init() {
		img.setImageBitmap(configs.render(baseBitmap));
		configs.setBuffer(baseBitmap);
	}
}

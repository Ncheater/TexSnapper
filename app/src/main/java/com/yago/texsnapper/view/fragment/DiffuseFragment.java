package com.yago.texsnapper.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.yago.texsnapper.GlobalContext;
import com.yago.texsnapper.MapType;
import com.yago.texsnapper.R;
import com.yago.texsnapper.model.DiffuseConfigs;

public class DiffuseFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

	private ImageView img;
	private SeekBar contrast, brightness, shadow, light;
	private DiffuseConfigs configs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_diffuse, container, false);
		img = v.findViewById(R.id.preview_pic);
		contrast = v.findViewById(R.id.diffuse_contrast);
		brightness = v.findViewById(R.id.diffuse_bright);
		shadow = v.findViewById(R.id.diffuse_shadow);
		light = v.findViewById(R.id.diffuse_light);

		contrast.setOnSeekBarChangeListener(this);
		brightness.setOnSeekBarChangeListener(this);
		shadow.setOnSeekBarChangeListener(this);
		light.setOnSeekBarChangeListener(this);

		assert getActivity() != null;
		configs = (DiffuseConfigs) ((GlobalContext) getActivity().getApplication()).getMap(MapType.DIFFUSE);

		refresh();
		return v;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (seekBar == contrast) {
			configs.setContrast(contrast.getProgress());
		} else if (seekBar == brightness) {
			configs.setBrightness(brightness.getProgress());
		} else if (seekBar == shadow) {
			configs.setShadow(shadow.getProgress());
		} else if (seekBar == light) {
			configs.setLight(light.getProgress());
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
		img.setImageBitmap(configs.render());
	}
}

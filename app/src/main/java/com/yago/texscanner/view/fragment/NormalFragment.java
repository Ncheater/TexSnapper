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
import com.vansuita.gaussianblur.GaussianBlur;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.MapType;
import com.yago.texscanner.R;
import com.yago.texscanner.model.NormalConfigs;

public class NormalFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

	private ImageView img;
	private SeekBar contrast, brightness, smoothness, strength;
	private NormalConfigs configs;
	private Bitmap baseBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_normal, container, false);
		img = v.findViewById(R.id.preview_pic);
		contrast = v.findViewById(R.id.normal_contrast);
		brightness = v.findViewById(R.id.normal_bright);
		smoothness = v.findViewById(R.id.normal_smoothness);
		strength = v.findViewById(R.id.normal_power);
		Switch invert = v.findViewById(R.id.normal_invertZ);

		contrast.setOnSeekBarChangeListener(this);
		brightness.setOnSeekBarChangeListener(this);
		smoothness.setOnSeekBarChangeListener(this);
		strength.setOnSeekBarChangeListener(this);
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
		configs = (NormalConfigs) ((GlobalContext) getActivity().getApplication()).getMap(MapType.NORMAL);
		baseBitmap = ((GlobalContext) getActivity().getApplication()).getMap(MapType.HEIGHT).getBuffer();

		init();
		return v;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (seekBar == contrast) {
			configs.setContrast(i);
		} else if (seekBar == brightness) {
			configs.setBrightness(i);
		} else if (seekBar == strength) {
			configs.setStrength(i);
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
		img.setImageBitmap(GaussianBlur.with(getContext()).radius(smoothness.getProgress() + 1).render(configs.render(baseBitmap)));
	}

	private void init() {
		img.setImageBitmap(GaussianBlur.with(getContext()).radius(smoothness.getProgress() + 1).render(configs.render(baseBitmap)));
	}
}

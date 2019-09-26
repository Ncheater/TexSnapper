package com.yago.texscanner.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.yago.texscanner.GlobalContext;
import com.yago.texscanner.MapType;
import com.yago.texscanner.R;

public class GlossinessFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_glossiness, container, false);
		ImageView img = v.findViewById(R.id.preview_pic);
		assert getActivity() != null;
		img.setImageBitmap(((GlobalContext) getActivity().getApplication()).getMap(MapType.GLOSSINESS).getMap());
		return v;
	}

}

package com.yago.texsnapper.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.yago.texsnapper.GlobalContext;
import com.yago.texsnapper.MapType;
import com.yago.texsnapper.R;

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

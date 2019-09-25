package com.yago.texsnapper.model;

import android.graphics.Bitmap;

public class NormalConfigs extends MapConfig {
	private int small = 0;
	private int big = 0;
	private int strenght = 0;
	private int smoothness = 0;
	private boolean inverted = false;

	public NormalConfigs(Bitmap map) {
		super(map);
	}

	public int getSmall() {
		return small;
	}

	public void setSmall(int small) {
		this.small = small;
	}

	public int getBig() {
		return big;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public int getStrenght() {
		return strenght;
	}

	public void setStrenght(int strenght) {
		this.strenght = strenght;
	}

	public int getSmoothness() {
		return smoothness;
	}

	public void setSmoothness(int smoothness) {
		this.smoothness = smoothness;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}
}

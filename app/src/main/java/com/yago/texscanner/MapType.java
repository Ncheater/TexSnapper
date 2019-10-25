package com.yago.texscanner;

public enum MapType {
	DIFFUSE(0), HEIGHT(1), ROUGHNESS(2), GLOSSINESS(3), NORMAL(4);

	private final int id;

	MapType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static MapType getTypeById(int id) {
		switch (id) {
			case 0:
				return MapType.DIFFUSE;
			case 1:
				return MapType.HEIGHT;
			case 2:
				return MapType.ROUGHNESS;
			case 3:
				return MapType.GLOSSINESS;
			case 4:
				return MapType.NORMAL;
			default:
				return null;
		}
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import static java.lang.String.format;

public class Color {

	private final int red;
	private final int green;
	private final int blue;

	public Color(String hex) {
		if (hex.length() != 7) throw new IllegalArgumentException("Hex values should be '#RRGGBB'");
		this.red = Integer.valueOf(hex.substring(1, 3), 16);
		this.green = Integer.valueOf(hex.substring(3, 5), 16);
		this.blue = Integer.valueOf(hex.substring(5, 7), 16);
	}

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int rgb() {
		return 255 << 24 | red << 16 | green << 8 | blue;
	}

	public int getRed() {
		return toUnsignedInt(red);
	}

	public int getGreen() {
		return toUnsignedInt(green);
	}

	public int getBlue() {
		return toUnsignedInt(blue);
	}

	private static int toUnsignedInt(int signed) {
		return signed & 0xFF;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Color color = (Color) o;
		return rgb() == color.rgb();
	}

	@Override
	public int hashCode() {
		return 31 * rgb();
	}

	@Override
	public String toString() {
		int red = toUnsignedInt(this.red);
		int green = toUnsignedInt(this.green);
		int blue = toUnsignedInt(this.blue);

		return format("#%02X%02X%02X", red, green, blue);
	}
}

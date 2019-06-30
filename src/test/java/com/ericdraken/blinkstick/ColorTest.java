/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ColorTest {

	@Test
	public void getRGBAsInt() {
		assertThat( Colors.Red.rgb(), is(java.awt.Color.RED.getRGB()));
		assertThat( Colors.Red.rgb(), is(-65536));
	}

	@Test
	public void getRGBFromHex() {
		assertThat( Colors.Red.getRed(), is(255));
		assertThat( Colors.Red.getGreen(), is(0));
		assertThat( Colors.Red.getBlue(), is(0));

		Color orange = new Color("#FFC800");
		assertThat(orange.getRed(), is(255));
		assertThat(orange.getGreen(), is(200));
		assertThat(orange.getBlue(), is(0));
	}

	@Test
	public void hexToString() {
		assertThat( Colors.Red.toString(), is("#FF0000"));
	}

	@Test
	public void equalityTest() {
		assertThat( Colors.Red, is(new Color("#FF0000")));
	}
}
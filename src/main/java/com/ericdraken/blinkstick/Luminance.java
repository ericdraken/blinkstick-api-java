/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import static java.awt.Color.HSBtoRGB;
import static java.awt.Color.RGBtoHSB;

/**
 * Use the java.awt.Color class to control the brightness of a given color
 */
public class Luminance
{
	public static Color setBrightness( Color color, double brightness )
	{
		return setBrightness( color.getRed(), color.getGreen(), color.getBlue(), brightness );
	}

	public static Color setBrightness( Color color, int brightness )
	{
		return setBrightness( color.getRed(), color.getGreen(), color.getBlue(), brightness );
	}

	public static Color setBrightness( int r, int g, int b, double brightness )
	{
		brightness = brightness < 0.0 ? 0.0 : brightness;
		brightness = brightness > 1.0 ? 1.0 : brightness;

		return setBrightness( r, g ,b, (int) Math.round( brightness * 255.0 ) );
	}

	public static Color setBrightness( int r, int g, int b, int brightness )
	{
		brightness = brightness < 0 ? 0 : brightness;
		brightness = brightness > 255 ? 255 : brightness;

		// Hue, saturation, brightness
		float[] vals = RGBtoHSB(r, g, b, null);
		int rgb = HSBtoRGB( vals[0], vals[1], brightness / 255f );
		java.awt.Color awtColor = new java.awt.Color( rgb );

		return new Color( awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue() );
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import static java.awt.Color.HSBtoRGB;
import static java.awt.Color.RGBtoHSB;

public class Effects
{
	private final BlinkStick stick;

	public Effects( final BlinkStick stick )
	{
		if ( stick == null )
		{
			throw new RuntimeException( "BlinkStick cannot be null" );
		}
		this.stick = stick;
	}

	/**
	 * Transition through the color wheel from one color to the next
	 *
	 * @param before     From color
	 * @param after      To color
	 * @param durationMs Transition duration
	 * @throws BlinkStickException Exception
	 */
	public void rainbowTransition( Color before, Color after, int durationMs ) throws BlinkStickException
	{
		long stepDelayMs = 10;
		float steps = durationMs / (float) stepDelayMs;

		float[] beforeVals = RGBtoHSB( before.getRed(), before.getGreen(), before.getBlue(), null );
		float[] afterVals = RGBtoHSB( after.getRed(), after.getGreen(), after.getBlue(), null );
		float[] deltas = new float[]{
			(afterVals[0] - beforeVals[0]) / steps,
			(afterVals[1] - beforeVals[1]) / steps,
			(afterVals[2] - beforeVals[2]) / steps,
		};

		for ( int i = 0; i < steps; i++ )
		{
			int rgb = HSBtoRGB(
				beforeVals[0] += deltas[0],
				beforeVals[1] += deltas[1],
				beforeVals[2] += deltas[2]
			);

			try
			{
				stick.setColor( rgb );
				Thread.sleep( stepDelayMs );
			}
			catch ( InterruptedException e )
			{
				// Early termination
				return;
			}
		}
	}

	/**
	 * Cross fade from one color to the next
	 *
	 * @param before     From color
	 * @param after      To color
	 * @param durationMs Transition duration
	 * @throws BlinkStickException Exception
	 */
	public void crossFade( Color before, Color after, int durationMs ) throws BlinkStickException
	{
		long stepDelayMs = 10;
		float steps = durationMs / (float) stepDelayMs;

		float rb = before.getRed();
		float gb = before.getGreen();
		float bb = before.getBlue();

		int ra = after.getRed();
		int ga = after.getGreen();
		int ba = after.getBlue();

		float rs = (ra - rb) / steps;
		float gs = (ga - gb) / steps;
		float bs = (ba - bb) / steps;

		for ( int i = 0; i < steps; i++ )
		{
			try
			{
				stick.setColor(
					Math.min( Math.max( Math.round( rb += rs ), 0 ), 255 ),
					Math.min( Math.max( Math.round( gb += gs ), 0 ), 255 ),
					Math.min( Math.max( Math.round( bb += bs ), 0 ), 255 )
				);

				// System.out.printf( "r=%f,g=%f,b=%f\n", rb, gb, bb );

				Thread.sleep( stepDelayMs );
			}
			catch ( InterruptedException e )
			{
				// Early termination
				return;
			}
		}
		stick.setColor( ra, ga, ba );    // Safety
	}

	/**
	 * Set the color between red, yellow, and green with a fraction from 0.0..1.0
	 *
	 * @param fraction   0.0..1.0
	 * @param brightness 0.0..1.0
	 * @throws BlinkStickException IO trouble
	 */
	public void setTrafficLightColor( double fraction, double brightness ) throws BlinkStickException
	{
		stick.setColor( colorFromTrafficLightGradient( fraction, brightness ) );
	}

	/**
	 * Set the color between red, yellow, and green with a fraction from 0.0..1.0
	 *
	 * @param fraction   0.0..1.0
	 * @param brightness 0.0..1.0
	 * @throws BlinkStickException IO trouble
	 */
	public void crossFadeToTrafficLightColor( double fraction, double brightness, int durationMs ) throws BlinkStickException
	{
		Color currColor = stick.getColorObj();
		Color newColor = colorFromTrafficLightGradient( fraction, brightness );
		crossFade( currColor, newColor, durationMs );
	}

	/**
	 * Get the color between red, yellow, and green with a fraction from 0.0..1.0
	 *
	 * @param fraction   0.0..1.0
	 * @param brightness 0.0..1.0
	 * @see [https://stackoverflow.com/a/6394340/1938889]
	 */
	public static Color colorFromTrafficLightGradient( double fraction, double brightness )
	{
		fraction = fraction < 0.0 ? 0.0 : fraction;
		fraction = fraction > 1.0 ? 1.0 : fraction;

		double red = (fraction > 0.5 ? 1 - 2 * (fraction - 0.5) : 1.0) * 255;
		double green = (fraction > 0.5 ? 1.0 : 2 * fraction) * 255;
		return Luminance.setBrightness( new Color( (int) red, (int) green, 0 ), brightness );
	}

	/**
	 * Turn on and off the BlinkStick to a given color for n pulses
	 * with a period duration between on complete cycle
	 *
	 * @param color    Color to strobe
	 * @param pulses   Number of pulses
	 * @param periodMs Period between one on and off cycle
	 * @throws BlinkStickException
	 */
	public void strobe( Color color, int pulses, int periodMs ) throws BlinkStickException
	{
		for ( int i = 0; i < pulses; i++ )
		{
			try
			{
				stick.setColor( color );
				Thread.sleep( periodMs / 2 );
				stick.turnOff();
				Thread.sleep( periodMs / 2 );
			}
			catch ( InterruptedException e )
			{
				// Early termination
				return;
			}
		}
	}
}

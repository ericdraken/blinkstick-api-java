/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.BlinkStickException;
import com.ericdraken.blinkstick.Luminance;
import com.ericdraken.blinkstick.Usb;

import static com.ericdraken.blinkstick.Colors.Green;

public class BrightnessDemo
{
	public static void main( String... args )
	{
		Usb.findFirstBlinkStick().map( stick -> {
			try
			{
				for ( int i = 0; i < 8; i++ )
				{
					for ( double b = 0.0; b <= 1.0; b += 0.01 )
					{
						stick.setColor( Luminance.setBrightness( Green, b ) );
						Sleep.sleep( 20 );
					}
				}
				stick.turnOff();
			}
			catch ( BlinkStickException e )
			{
				e.printStackTrace();
			}
			return null;
		} );
	}
}

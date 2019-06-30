/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.Colors;
import com.ericdraken.blinkstick.Usb;

// Try-with-resources multiple open and close demo
public class MultipleOpenTWRDemo
{
	public static void main( String... args )
	{
		for ( int i = 0; i < 3; i++ )
		{
			// Java 9 and higher
			Usb.findAllBlinkSticks().forEach( blinkStick -> {
				try ( blinkStick )
				{
					blinkStick.setColor( Colors.Blue );

					System.out.printf(
						"%n%s, colour %s%n",
						blinkStick.getProductDescription(),
						blinkStick.getColorObj()
					);

					Sleep.sleep( 100 );

					blinkStick.turnOff();
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			} );
		}
	}
}

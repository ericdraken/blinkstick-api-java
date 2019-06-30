/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.Color;
import com.ericdraken.blinkstick.Colors;
import com.ericdraken.blinkstick.Pattern;
import com.ericdraken.blinkstick.Usb;

import java.util.Random;

// Set random colors with and without a pattern buffer
public class RandomColors
{
	public static void main( String... args )
	{
		// Max 62 colors at a time for the pattern buffer
		int maxColors = 62;

		// Report 1
		System.out.println( "Host-based color control" );
		Usb.findFirstBlinkStick( true ).map( stick -> {
			try( stick )
			{
				for ( int i = 0; i < maxColors; i++ )
				{
					stick.setRandomColor();
					Sleep.sleep( 100 );
				}
				stick.turnOff();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			return null;
		} );
		System.out.println( "  Finished" );

		Sleep.sleep( 1000 );

		// Report 20
		System.out.println( "ATTiny85-based color control" );
		Usb.findFirstBlinkStick( true ).map( stick -> {
			try( stick )
			{
				int delay = 100;
				Pattern pattern = new Pattern();
				Random random = new Random();

				for ( int i = 0; i < maxColors-1; i++ )
				{
					pattern.addColorAndDuration(
						new Color( random.nextInt( 256 ), random.nextInt( 256 ), random.nextInt( 256 ) ),
						delay
					);
				}

				// Turn off
				pattern.addColorAndDuration( Colors.Black, 0 );
				stick.setBlinkPattern( pattern );
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			return null;
		} );
		System.out.println( "  Uploaded and done" );
	}
}

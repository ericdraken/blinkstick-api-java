/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.*;

public class StressTest
{
	public static void main( String... args )
	{
		Usb.findFirstBlinkStick( true ).map( stick -> {
			for ( int round = 0; round < 1000; round++ )
			{
				System.out.println( "Color pattern upload (non-blocking of host CPU)..." );
				try
				{
					Pattern pattern = new Pattern();

					stick.setColor( Colors.Black );

					int max = (255 / 30) * 30;

					for ( int i = 0; i <= max; i += 30 )
					{
						pattern.addColorAndDuration( new Color( i, 0, 0 ), 50 );
					}

					for ( int i = max; i >= 0; i -= 30 )
					{
						pattern.addColorAndDuration( new Color( i, 0, 0 ), 50 );
					}

					stick.setBlinkPattern( pattern );
					System.out.println( "  Uploaded" );

					// Demonstrate the non-blocking IO and how the BlinkStick
					// ignores uploads while it is busy rendering a pattern
					System.out.println( "Upload the same pattern repeatedly..." );
					for ( int count = 0; count < 10; count++ )
						stick.setBlinkPattern( pattern );
					System.out.println( "  Finished" );

					// Inspect the current color
					for ( int count = 0; count < 10; count++ )
					{
						System.out.println( "Current color: " + stick.getColorObj() );
						Sleep.sleep( 50 );
					}
					System.out.println( "Finished" );
				}
				catch ( BlinkStickException e )
				{
					e.printStackTrace();
				}

				/* Block the host CPU to manage the BlinkStick colors and timings */

				System.out.println( "Cross-fading colors from red to green (blocks host CPU)..." );
				try
				{
					for ( double f = 0.0; f <= 1.0; f += 0.1 )
					{
						new Effects( stick ).crossFadeToTrafficLightColor( f, 0.8, 200 );
						System.out.println( "  Current color: " + stick.getColorObj() );
					}
					stick.turnOff();
					System.out.println( "  Finished" );
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
			return null;
		} );
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.Colors;
import com.ericdraken.blinkstick.Pattern;
import com.ericdraken.blinkstick.Usb;

public class PatternDemo
{
	public static void main( String... args )
	{
		Usb.findFirstBlinkStick( true ).map( stick -> {
			try ( stick )
			{
				// Pulse the 7 colors of the rainbow and turn off
				int delay = 200;
				Pattern pattern = new Pattern();
				pattern
					.addColorAndDuration( Colors.Red, delay )
					.addColorAndDuration( Colors.Black, delay )
					.addColorAndDuration( Colors.Orange, delay )
					.addColorAndDuration( Colors.Black, delay )
					.addColorAndDuration( Colors.Yellow, delay )
					.addColorAndDuration( Colors.Black, delay )
					.addColorAndDuration( Colors.Green, delay )
					.addColorAndDuration( Colors.Black, delay )
					.addColorAndDuration( Colors.Blue, delay )
					.addColorAndDuration( Colors.Black, delay )
					.addColorAndDuration( Colors.Indigo, delay )
					.addColorAndDuration( Colors.Black, delay )
					.addColorAndDuration( Colors.Violet, delay )
					.addColorAndDuration( Colors.Black, delay );

				stick.setBlinkPattern( pattern );

				// Get the saved pattern
				System.out.println( stick.getBlinkPattern() );
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			return null;
		} );
	}
}

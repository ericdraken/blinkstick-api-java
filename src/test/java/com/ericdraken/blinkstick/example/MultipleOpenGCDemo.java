/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.Usb;

// Multiple open with closing triggered by the garbage collector
public class MultipleOpenGCDemo
{
	public static void main( String... args )
	{
		for ( int i = 0; i < 4; i++ )
		{
			// Java 9 and higher
			Usb.findAllBlinkSticks().forEach( blinkStick -> {
				try
				{
					System.out.printf(
						"%n%s, colour %s%n",
						blinkStick.getProductDescription(),
						blinkStick.getColorObj()
					);
				} catch ( Exception e ){
					e.printStackTrace();
				}
			} );

			System.gc();	// This will force the cleaners to close connections
		}
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.Usb2;

// Non-singleton try-with-resources demo
public class MultipleOpenUsb2TWRDemo
{
	public static void main( String... args ) throws Exception
	{
		for ( int i = 0; i < 4; i++ )
		{
			try ( Usb2 usb2 = new Usb2() )
			{
				// Java 9 and higher
				usb2.findAllBlinkSticks().forEach( blinkStick -> {
					try
					{
						System.out.printf(
							"%n%s, colour %s%n",
							blinkStick.getProductDescription(),
							blinkStick.getColorObj()
						);
					}
					catch ( Exception e )
					{
						e.printStackTrace();
					}
				} );
			}

			System.gc();	// This will force the cleaners to close connections
		}
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import java.util.List;
import java.util.stream.Collectors;

import static com.ericdraken.blinkstick.Usb.findAllBlinkSticks;
import static java.lang.String.*;

public class BlinkStickInfoDemo
{
	public static void main( String... args )
	{
		List<String> devices = findAllBlinkSticks()
			.stream()
			.map( stick ->
				format( "Product      : %s%n", stick.getProductDescription() ) +
				format( "Serial #     : %s%n", stick.getSerial() ) +
				format( "Manufacturer : %s%n", stick.getManufacturer() ) ).collect( Collectors.toList() );

		devices.forEach( System.out::println );

		if ( devices.isEmpty() )
		{
			System.out.println( "No BlinkSticks found" );
		}
	}
}

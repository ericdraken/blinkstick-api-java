/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick.example;

import com.ericdraken.blinkstick.BlinkStickException;
import com.ericdraken.blinkstick.Colors;
import com.ericdraken.blinkstick.Effects;
import com.ericdraken.blinkstick.Usb;

public class EffectsDemo
{
	public static void main( String... args )
	{
		Usb.findFirstBlinkStick( true ).ifPresent( stick -> {
			try
			{
				System.out.println( "Traffic lights 1" );
				try
				{
					for ( double f = 0.0; f <= 1.0; f += 0.1 )
					{
						new Effects( stick ).crossFadeToTrafficLightColor( f, 0.8, 1000 );
					}
					stick.turnOff();
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}

				System.out.println( "Traffic lights 2" );
				try
				{
					for ( double f = 0.0; f <= 1.0; f += 0.005 )
					{
						new Effects( stick ).setTrafficLightColor( f, 0.8 );
						Thread.sleep( 20 );
					}
					for ( double f = 1.0; f >= 0.0; f -= 0.005 )
					{
						new Effects( stick ).setTrafficLightColor( f, 0.8 );
						Thread.sleep( 20 );
					}
					Thread.sleep( 500 );
					stick.turnOff();
				}
				catch ( BlinkStickException | InterruptedException e )
				{
					e.printStackTrace();
				}

				System.out.println( "Cross-fade" );
				try
				{
					new Effects( stick ).crossFade( Colors.Red, Colors.Blue, 1000 );
					stick.turnOff();
				}
				catch ( BlinkStickException e )
				{
					e.printStackTrace();
				}

				System.out.println( "Rainbow transition" );
				try
				{
					new Effects( stick ).rainbowTransition( Colors.Red, Colors.Blue, 1000 );
					stick.turnOff();
				}
				catch ( BlinkStickException e )
				{
					e.printStackTrace();
				}

				System.out.println( "Quick pulses" );
				try
				{
					new Effects( stick ).strobe( Colors.Green, 10, 100 );
					stick.turnOff();
				}
				catch ( BlinkStickException e )
				{
					e.printStackTrace();
				}

				System.out.println( "Slow pulses" );
				try
				{
					new Effects( stick ).strobe( Colors.Blue, 4, 1000 );
					stick.turnOff();
				}
				catch ( BlinkStickException e )
				{
					e.printStackTrace();
				}
			}
			finally
			{
				try
				{
					stick.turnOff();
				}
				catch ( BlinkStickException e )
				{
					e.printStackTrace();
				}
			}
		} );
	}
}

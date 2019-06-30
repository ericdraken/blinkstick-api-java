/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import org.hid4java.HidDevice;

import java.lang.ref.Cleaner;
import java.util.Arrays;
import java.util.Random;

public class BlinkStickAPI implements BlinkStick
{
	private final HidDevice device;

	private final Cleaner.Cleanable cleanable;

	private static final Cleaner cleaner = Cleaner.create();

	private static final int PATTERN_BUFFER_LENGTH = 1 + 1 + (Pattern.MAX_PATTERN_COUNT*4);   // (count + (r,g,b,delay)*10)

	private static final int REPLY_BUFFER_LENGTH = 33;

	BlinkStickAPI( final HidDevice device )
	{
		if ( device.getVendorId() != Device.BlinkStickVendorId || device.getProductId() != Device.BlinkStickProductId )
		{
			throw new RuntimeException( "This is not a BlinkStick" );
		}

		this.device = device;

		// Close this device on GC
		cleanable = cleaner.register( this, device::close );
	}

	/**
	 * Set the color of the device with separate r, g and b byte values
	 *
	 * @param r red byte color value 0..255
	 * @param g green byte color value 0..255
	 * @param b blue byte color value 0..255
	 */
	@Override
	public void setColor( int r, int g, int b ) throws BlinkStickException
	{
		try
		{
			device.sendFeatureReport(
				new byte[]{(byte) r, (byte) g, (byte) b},
				(byte) 1
			);
		}
		catch ( Exception e )
		{
			throw new BlinkStickException( "Unable to set color", e );
		}
	}

	/**
	 * Set the color of the device from a 24-bit int
	 *
	 * @param value color as int
	 */
	@Override
	public void setColor( int value ) throws BlinkStickException
	{
		int r = (value >> 16) & 0xFF;
		int g = (value >> 8) & 0xFF;
		int b = value & 0xFF;

		setColor( r, g, b );
	}

	/**
	 * Set an LED color
	 *
	 * @param color Color
	 * @throws BlinkStickException Exception
	 */
	@Override
	public void setColor( Color color ) throws BlinkStickException
	{
		setColor( color.rgb() );
	}

	/**
	 * Set the LED to a random color
	 *
	 * @throws BlinkStickException Exception
	 */
	@Override
	public void setRandomColor() throws BlinkStickException
	{
		Random random = new Random();
		setColor( random.nextInt( 256 ), random.nextInt( 256 ), random.nextInt( 256 ) );
	}

	/**
	 * Turn off the Digistump by setting the RGB intensity to 0, or black
	 *
	 * @throws BlinkStickException Exception
	 */
	@Override
	public void turnOff() throws BlinkStickException
	{
		setColor( 0, 0, 0 );
	}

	/**
	 * Get the color as a 24-bit int
	 *
	 * @return int
	 * @throws BlinkStickException Exception
	 */
	@Override
	public int getColor() throws BlinkStickException
	{
		byte[] data = new byte[REPLY_BUFFER_LENGTH];

		try
		{
			int read = device.getFeatureReport( data, (byte) 1 );
			if ( read > 0 )
			{
				return (255 << 24) | (data[1] << 16) | (data[2] << 8) | data[3];
			}
		}
		catch ( Exception e )
		{
			throw new BlinkStickException( "Unable to get color", e );
		}

		return 0;
	}

	/**
	 * Return a Color object from the BlinkStick
	 *
	 * @return Color object or null
	 * @throws BlinkStickException IO trouble
	 */
	@Override
	public Color getColorObj() throws BlinkStickException
	{
		byte[] data = new byte[REPLY_BUFFER_LENGTH];

		try
		{
			int read = device.getFeatureReport( data, (byte) 1 );
			if ( read > 0 )
			{
				return new Color( data[0], data[1], data[2] );
			}
		}
		catch ( Exception e )
		{
			throw new BlinkStickException( "Unable to get color", e );
		}

		return new Color( 0, 0, 0 );
	}

	/**
	 * Get the Digistump maker
	 *
	 * @return String
	 */
	@Override
	public String getManufacturer()
	{
		try
		{
			return device.getManufacturer();
		}
		catch ( Exception e )
		{
			return "<unknown manufacturer>";
		}
	}

	/**
	 * Get the Digistump device description
	 *
	 * @return String
	 */
	@Override
	public String getProductDescription()
	{
		try
		{
			return device.getProduct();
		}
		catch ( Exception e )
		{
			return "<unknown product>";
		}
	}

	/**
	 * Get the serial number
	 * @return String
	 */
	@Override
	public String getSerial()
	{
		try
		{
			return device.getSerialNumber();
		}
		catch ( Exception e )
		{
			return "<unknown serial>";
		}
	}

	/**
	 * Upload a color pattern to the DigiStunp
	 *
	 * @param patternBuffer Pattern
	 * @throws BlinkStickException Exception
	 */
	@Override
	public void setBlinkPattern( Pattern patternBuffer ) throws BlinkStickException
	{
		byte[] data = new byte[PATTERN_BUFFER_LENGTH];

		int index = 0;
		data[index++] = (byte)patternBuffer.getPatternCount(); // Pattern count (0-10)

		for ( Pattern.PatternPair pair : patternBuffer.getPatternPairs() )
		{
			data[index++] = (byte)pair.getColor().getRed();
			data[index++] = (byte)pair.getColor().getGreen();
			data[index++] = (byte)pair.getColor().getBlue();
			data[index++] = (byte)pair.getDuration();
		}

		try
		{
			device.sendFeatureReport( data, (byte) 20 );
		}
		catch ( Exception e )
		{
			String objStr = Arrays.deepToString( patternBuffer.getPatternPairs().toArray() );
			throw new BlinkStickException( "Unable to set pattern: " + objStr, e );
		}
	}

	/**
	 * Recover the color pattern currently in the Digistump device
	 *
	 * @return Pattern
	 * @throws BlinkStickException Exception
	 */
	@Override
	public Pattern getBlinkPattern() throws BlinkStickException
	{
		byte[] data = new byte[PATTERN_BUFFER_LENGTH];

		try
		{
			int read = device.getFeatureReport( data, (byte) 20 );
			if ( read > 0 )
			{
				int count = data[0];
				int offset = 1;
				Pattern pattern = new Pattern();

				// Build up the color pattern from the raw byte array
				for( int i = 0; i < count; i++ )
				{
					pattern.addColorAndDuration(
						new Color( data[offset++], data[offset++], data[offset++] ),
						data[offset++] * 10
					);
				}
				return pattern;
			}
		}
		catch ( Exception e )
		{
			throw new BlinkStickException( "Unable to get pattern count: " + e );
		}

		return new Pattern();
	}

	/**
	 * Close the USB connection
	 */
	@Override
	public void close()
	{
		// Invoke the cleaner right away to close the device
		cleanable.clean();
	}
}

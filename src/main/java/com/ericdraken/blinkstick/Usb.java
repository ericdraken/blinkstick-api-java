/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import org.hid4java.HidDevice;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;

import java.util.List;
import java.util.Optional;

/**
 * Singleton version of Usb2
 */
public class Usb
{
	private static final Usb2 usb2;

	static
	{
		// Default operation
		HidServicesSpecification spec = new HidServicesSpecification();
		spec.setScanMode( ScanMode.SCAN_AT_FIXED_INTERVAL );
		spec.setAutoShutdown( true );
		usb2 = new Usb2( spec );
	}

	/**
	 * Return a list of all the USB devices
	 *
	 * @return List
	 */
	public static List<Device> devices()
	{
		return usb2.devices();
	}

	/**
	 * Return a list of all the raw USB HID devices
	 *
	 * @return List
	 */
	public static List<HidDevice> rawDevices()
	{
		return usb2.rawDevices();
	}

	/**
	 * Get the num of attached BlinkSticks
	 *
	 * @return Number of BlinkSticks
	 */
	public static int numBlinkSticksPresent()
	{
		return usb2.numBlinkSticksPresent();
	}

	/**
	 * Find and open the first BlinkStick with a rate limiter
	 *
	 * @return Optional<BlinkStick>
	 */
	public static Optional<BlinkStick> findFirstBlinkStick()
	{
		return findFirstBlinkStick( true );
	}

	/**
	 * Find and open the first BlinkStick with a rate limiter
	 *
	 * @param rateLimit Use a rate limiter or not
	 * @return Optional<BlinkStick>
	 */
	public static Optional<BlinkStick> findFirstBlinkStick( boolean rateLimit )
	{
		return usb2.findFirstBlinkStick( rateLimit );
	}

	/**
	 * Find and open a BlinkStick by serial and return a rate limiter
	 *
	 * @param serial Serial number to search
	 * @return Optional<BlinkStick>
	 */
	public static Optional<BlinkStick> findBlinkStickBy( String serial )
	{
		return findBlinkStickBy( serial, true );
	}

	/**
	 * Find and open a BlinkStick by serial and return an optional rate limiter
	 *
	 * @param serial Serial number to search
	 * @param rateLimit Use a rate limiter or not
	 * @return Optional<BlinkStick>
	 */
	public static Optional<BlinkStick> findBlinkStickBy( String serial, boolean rateLimit )
	{
		return usb2.findBlinkStickBy( serial, rateLimit );
	}

	/**
	 * Find and open all BlinkSticks
	 *
	 * @return List<BlinkStick>
	 */
	public static List<BlinkStick> findAllBlinkSticks()
	{
		return findAllBlinkSticks( true );
	}

	/**
	 * Find and open all BlinkSticks
	 *
	 * @param rateLimit Use a rate limiter or not
	 * @return List<BlinkStick>
	 */
	public static List<BlinkStick> findAllBlinkSticks( boolean rateLimit )
	{
		return usb2.findAllBlinkSticks( rateLimit );
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import org.hid4java.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Non-singleton HidServices controller
 */
public class Usb2 implements AutoCloseable
{
	private HidServices hidServices = null;

	private HidServicesSpecification spec;

	public Usb2()
	{
		HidServicesSpecification spec = new HidServicesSpecification();
		spec.setScanMode( ScanMode.NO_SCAN );
		spec.setAutoShutdown( false );
		setSpec( spec );
	}

	public Usb2( HidServicesSpecification spec )
	{
		setSpec( spec );
	}

	private synchronized void setSpec( HidServicesSpecification spec )
	{
		this.spec = spec;
	}

	/**
	 * Return a list of all the USB devices
	 *
	 * @return List
	 */
	public synchronized List<Device> devices()
	{
		return findAllDevices()
			.stream()
			.map( info -> new Device(
					info.getManufacturer(),
					info.getProduct(),
					info.getSerialNumber(),
					info.getVendorId(),
					info.getProductId()
				)
			)
			.collect( toList() );
	}

	/**
	 * Return a list of all the raw USB HID devices
	 *
	 * @return List
	 */
	public synchronized List<HidDevice> rawDevices()
	{
		return findAllDevices();
	}

	/**
	 * Get the num of attached BlinkSticks
	 *
	 * @return Number of BlinkSticks
	 */
	public synchronized int numBlinkSticksPresent()
	{
		return findAllBlinkStickDevices().size();
	}

	/**
	 * Find and open the first BlinkStick with a rate limiter
	 *
	 * @return Optional<BlinkStick>
	 */
	public synchronized Optional<BlinkStick> findFirstBlinkStick()
	{
		return findFirstBlinkStick( true );
	}

	/**
	 * Find and open the first BlinkStick with a rate limiter
	 *
	 * @param rateLimit Use a rate limiter or not
	 * @return Optional<BlinkStick>
	 */
	public synchronized Optional<BlinkStick> findFirstBlinkStick( boolean rateLimit )
	{
		return findAllBlinkStickDevices()
			.stream()
			.findFirst()
			.map( hidDeviceInfo -> createBlinkStick( hidDeviceInfo, rateLimit ) );
	}

	/**
	 * Find and open a BlinkStick by serial and return a rate limiter
	 *
	 * @param serial Serial number to search
	 * @return Optional<BlinkStick>
	 */
	public synchronized Optional<BlinkStick> findBlinkStickBy( String serial )
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
	public synchronized Optional<BlinkStick> findBlinkStickBy( String serial, boolean rateLimit )
	{
		return findAllBlinkStickDevices()
			.stream()
			.filter( info -> info.getSerialNumber().equals( serial ) )
			.map( hidDeviceInfo -> createBlinkStick( hidDeviceInfo, rateLimit ) )
			.findFirst();
	}

	/**
	 * Find and open all BlinkSticks
	 *
	 * @return List<BlinkStick>
	 */
	public synchronized List<BlinkStick> findAllBlinkSticks()
	{
		return findAllBlinkSticks( true );
	}

	/**
	 * Find and open all BlinkSticks
	 *
	 * @param rateLimit Use a rate limiter or not
	 * @return List<BlinkStick>
	 */
	public synchronized List<BlinkStick> findAllBlinkSticks( boolean rateLimit )
	{
		return findAllBlinkStickDevices()
			.stream()
			.map( hidDeviceInfo -> createBlinkStick( hidDeviceInfo, rateLimit ) )
			.collect( toList() );
	}

	/**
	 * <p>Simple service provider providing generally safe defaults. If you find you are experiencing problems, particularly
	 * with constrained devices, consider exploring the {@link HidServicesSpecification} options.</p>
	 *
	 * @return A single instance of the HID services using the default specification
	 */
	public synchronized HidServices getHidServices() throws HidException
	{
		if (hidServices == null)
		{
			hidServices = new HidServices(spec);
		}

		return hidServices;
	}

	@Override
	public void close() throws Exception
	{
		if ( hidServices != null )
		{
			hidServices.shutdown();	// close and api exit
		}
	}

	/* Private */

	private synchronized List<HidDevice> findAllDevices()
	{
		return getHidServices().getAttachedHidDevices();
	}

	private synchronized List<HidDevice> findAllBlinkStickDevices()
	{
		return findAllDevices()
			.stream()
			.filter( info -> info.getVendorId() == Device.BlinkStickVendorId && info.getProductId() == Device.BlinkStickProductId )
			.collect( toList() );
	}

	private synchronized BlinkStick createBlinkStick( HidDevice device, boolean rateLimit )
	{
		if ( device == null || !device.open() )
		{
			throw new NullPointerException( "Failed to open USB device, the native open() method returned null. Could be a OS/driver issue, check your library path. Also the USB device may have been left open." );
		}

		BlinkStick bsReal = new BlinkStickAPI( device );
		return rateLimit ? new RateLimitedBlinkStick( bsReal ).createProxy() : bsReal;
	}
}

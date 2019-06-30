/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 *
 * Original class from bad.robot.blinkstick
 */

package com.ericdraken.blinkstick;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class RateLimitedBlinkStick implements InvocationHandler
{
	private static final long MinimumDelayMS = 10;

	private final BlinkStick delegate;

	private long timeOfLastCall = System.currentTimeMillis();

	public RateLimitedBlinkStick( BlinkStick delegate )
	{
		this.delegate = delegate;
	}

	public BlinkStick createProxy()
	{
		return (BlinkStick) Proxy.newProxyInstance( BlinkStick.class.getClassLoader(), new Class<?>[]{BlinkStick.class}, this );
	}

	public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
	{
		long timeSinceLastCall = System.currentTimeMillis() - timeOfLastCall;
		if ( timeSinceLastCall < MinimumDelayMS )
		{
			try
			{
				Thread.sleep( MinimumDelayMS - timeSinceLastCall );
			}
			catch ( InterruptedException e )
			{
				Thread.currentThread().interrupt();
			}
		}

		try
		{
			return method.invoke( delegate, args );
		}
		finally
		{
			timeOfLastCall = System.currentTimeMillis();
		}
	}
}

/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

public interface BlinkStick extends AutoCloseable
{
	void setColor( int r, int g, int b ) throws BlinkStickException;

	void setColor(int value) throws BlinkStickException;

	void setColor( Color color ) throws BlinkStickException;

	void setRandomColor() throws BlinkStickException;

	void turnOff() throws BlinkStickException;

	int getColor() throws BlinkStickException;

	Color getColorObj() throws BlinkStickException;

	String getManufacturer();

	String getProductDescription();

	String getSerial();

	// Patterns
	void setBlinkPattern( Pattern patternBuffer ) throws BlinkStickException;

	Pattern getBlinkPattern() throws BlinkStickException;

	void close() throws Exception;
}

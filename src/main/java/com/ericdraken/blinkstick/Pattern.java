/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class Pattern
{
	public static final int MAX_PATTERN_COUNT = 62;

	private static final int MAX_DURATION = 2_550;

	private LinkedList<PatternPair> patterns = new LinkedList<>();

	public Pattern addColorAndDuration( Color color, int durationMs )
	{
		// Only so many patterns are allowed
		if ( patterns.size() >= MAX_PATTERN_COUNT )
			return this;

		// Bound the duration
		durationMs = durationMs < 0 ? 0 : durationMs;
		durationMs = durationMs > MAX_DURATION ? MAX_DURATION : durationMs;

		// Lower the resolution to fit in a char
		durationMs /= 10;

		patterns.add( new PatternPair( color, durationMs ) );
		return this;
	}

	public LinkedList<PatternPair> getPatternPairs()
	{
		return patterns;
	}

	public int getPatternCount()
	{
		return patterns.size();
	}

	@Override
	public String toString()
	{
		return patterns
			.stream()
			.map( PatternPair::toString )
			.collect(
				Collectors.joining( String.format( ",%n" ), "[", "]" )
			);
	}

	public class PatternPair
	{
		private final Color color; //color member of pair
		private final int duration; //duration member of pair

		private PatternPair( Color first, int second) {
			this.color = first;
			this.duration = second;
		}

		public Color getColor() {
			return color;
		}

		public int getDuration() {
			return duration;
		}

		@Override
		public String toString() {
			return "PatternPair{" +
				"color=" + color +
				", duration=" + duration +
				'}';
		}
	}
}

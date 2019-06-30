/*
 * Copyright (c) 2019. Eric Draken - ericdraken.com
 */

package com.ericdraken.blinkstick;

public class BlinkStickException extends Exception{

    public BlinkStickException( String message) {
        super(message);
    }

    public BlinkStickException( String message, Throwable cause) {
        super(message, cause);
    }
}

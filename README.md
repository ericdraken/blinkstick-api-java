BlinkStick Java API with Color Pattern Buffer Support
====

A Java library to control Digistump ATTiny85-based [BlinkStick](http://www.blinkstick.com) devices via USB to
set a LED color or execute a color pattern directly on the Digistump.

Features
=======

* Uses `org.hid4java.HidDevice` instead of `com.codeminders.hidapi`
* Includes effects like cross-fade and strobe
* Has support for uploading a color pattern when using the [color-patten firmware](https://github.com/ericdraken/digispark-firmware-attiny85) fork
* Has try-with-resources support to close the USB properly

Examples
=====

See the `examples` folder for several examples setting colors and patterns.

Images
=======

Here is a device with the [color-pattern-buffer](https://github.com/ericdraken/digispark-firmware-attiny85) firmware with which to use this Java API.

![Digispark USB with custom soldered APA106 LED](/pictures/attiny85-front.jpg)
![Digispark parts placement](/pictures/attiny85-back.jpg)

Running in Docker
==========

This is helpful:

```shell script
# Install libudev and libusb-1.0
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    libudev-dev libusb-1.0-0 \
    && rm -rf /var/lib/apt/lists/*

# Link libudev.so.1 to libudev.so.0
RUN ln -sf $(find /lib -name libudev.so) $(find /lib -name libudev.so).0
```

Run `docker run --device=/dev/bus ...` to mount the USB devices.

Acknowledgements
========

The API is based on the the [BlinkStick Android](https://github.com/arvydas/blinkstick-android/) and [BlinkStick for Processing](https://github.com/arvydas/blinkstick-processing) libraries by [arvydas](https://github.com/arvydas).

Part of this fork is based off the fork from [Flewp/blinkstick-java](https://github.com/Flewp/blinkstick-java) which adds a rate limiter when communicating with the WS2812. This version works well on Windows 10.

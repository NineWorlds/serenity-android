Serenity for Google TV
=======================

Serenity for Google TV is a Plex Media Server client.  It is an alternative
client to the offical client from Plex, Inc.   Serenity is not associated with
Plex, Inc.   If you value the Plex Media Server, I highly recommend making a 
donation to that project to help fund continued development.

Features
----

What will this support:

* Browsing existing TV Show and Movie Libraries (implemented)
* Playing back Movies and TV Shows (implemented)
* Browsing by Genre for TV Shows and Movies (inprogress)
* Browsing by Season for TV Shows (implemented)
* Queuing of TV Shows for play back (i.e. playback and entire season back to back)
* Browsing Video Channels
* Playback of video channels
* Browsing Music

What is currently not targeted:

* MyPlex access
* Photo Browsing
* Adding/Managing Channels and addins

With this said, this is an open source project, so if the community wants to contribute
code it will be welcomed.  Feature requests and bug reports can be opened on the issue
tracker.

Releases
----

0.5.0

This is the first version that can be used to Browse and Playback TV Shows and Movies. Even though in an Alpha state it is stable enough for daily use.

Download: http://kingargyle.github.com/googletv/serenity/alpha/Serenity-0.5.0.apk

_You will need to sideload this app, it is not available in the playstore_

Report Issues at: https://github.com/kingargyle/plexapp-client/issues

* Server Preferences - Plex Media Server IP Address and Port number.  Access either through Menu key, or Settings main menu
* Tested and developed using a Vizio Costar and Plex Media Server 0.9.7.x, other versions may work.
* Implements Movie Browsing and playback through external video player.
  * Works with aVia or ViMu video players for direct play (no transcoding)
* Browse TV Shows, By Season, or All Episodes.
* Implements a very visual but simple interface.  When in Episode or Movie Browsing just Click (enter, OK) the episode/movie you want to watch.
* To leave a screen use the Back key on your remote.
* This program is very graphic intensive and uses the SD card of your device as a cache. Items are cached for up to 7 days.  It is not uncommon for it to use between 80 to 150 meg for the cache.  The caching and downloading of the images happens as needed so it will greatly depend on the size of your collection.

This needs people test it and kick it's tires.  I only have a Vizio Costar to test and develop with so, I need people with other devices to help test this out.  I have tested against Plex Media Server for Linux v0.9.7.11.  If you run into issues please file a bug with the version of plex media server, and the google tv device (model number as well) that you are using.  Please be as descriptive as possible when reporting issues.



Plexapp REST Library
----

This project includes a module that provides READ access to the plex media server REST API.
It can be use outside of the serenity application by others developers to access the metadata provided by a Plex Media Server.  The code is licensed under an MIT License.


FAQ
===

Why build Serenity?
----

Mainly because I have gotten tired of apps for Google TV that are still assuming a 
tablet interface.  I also wanted most of the features listed above, but I also
wanted something that looked really good.  I'm inspired by many of the very excellently
designed skins for XBMC and the ports that have been done for Plex Media Center.  I feel
that the 10 ft experience isn't currently being addressed well with the current Plex app, and
since it isn't open source there is no way to contribute code to help improve it.

I also wanted to learn android development, and had an itch to scratch.  Instead of complaining
that their aren't any apps for Google TV, I decided to write one myself.

What open source license is this using?
-----

I'm using one of the more liberal open source licenses available. MIT.

http://opensource.org/licenses/MIT

Yes this means anybody can fork the project, and try to do their own client.  Commercial opportunities though
will be limited since the app will more than likely use the free Transcoder KEY that PlexApp provides.  Any
commercial resell of an app that uses the transcoder features of Plex needs a special key from Plex.  Thus
one of the reasons this project is open source and will probably stay free in the play store once it is
released there.


Will this be on play store?
-----

Eventually it will be.  It needs to get to a point where it is feature rich enough and stable enough
to put it out there for the masses.

Do you need testers?
-----

Eventually yes. Currently the project isn't to the point where there is much beyond movie browsing to test.
Once play back is implemented, then I'll put out an APK that can be downloaded for those that want to test
it out.

How can I help?
-----

If you are a programmer, fork the project, and provide patches or enhancements via pull requests.
If you don't have coding skills, but have graphic design skills, the project can always use a Logo, Icons, etc.
Otherwise, file bugs, and open enhancement requests.   I'm looking into various ways for donations to be sent
to help the project along as well.   The app will probably be free in the playstore.


Building from Source
=============

This requires that you have used the maven-android-sdk-deployer project to
install and deploy the Android 3.2 SDK to your maven repository.  

https://github.com/mosabua/maven-android-sdk-deployer

Make sure to set the ANDROID_HOME environement variable to the location where your SDK is deployed.


Maven 3.x is required as well.

http://maven.apache.org/download.cgi

To build the APK and APKLibraries from the command line:

mvn clean install

The application APK will be in PlexAppClient/target.  You can sideload this APK on your Google TV device.

Note: Currently you need to change the config.properties file in the PlexAppClient/src/main/resources directory
so that it points to your Plex Media Server's ip address.  A Settings configuration screen will be provided
in the future to all you to set this through the app.


Open Source Projects:
=====

This project uses several open source projects and source code:

* LeftNavBarLibrary (http://code.google.com/p/googletv-android-samples/) from the google tv project.  This is packaged as a apklib for reuse.
* ImageLoader (https://github.com/novoda/ImageLoader) - a nice library for managing image caches and downloading of images in the background.
* Simple (http://simple.sourceforge.net/) - provides a nice interface to Serialize and Deserialize XML information.

Unit Testing Frameworks
------
* Robolectric (http://pivotal.github.com/robolectric/) - Android Integration/Unit Testing framework that allows testing without launching an emulator.
* XMLUnit (http://xmlunit.sourceforge.net/) - unit testing framework to enhaced xml file verification.

Skins
====

Some layouts are influenced by the following skins.  Some icons reused from the Influence skin for XBMC

* Aeon
* MediaStreamer
* Influence

Creative Commons
======

Some icons are used under the Creative Commons Attribution License.

Fuge Icons (http://p.yusukekamiyamane.com/) by Yusuke Kamiyamane


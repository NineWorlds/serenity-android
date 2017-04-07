Serenity for Android
=======================

[![Build Status](https://travis-ci.org/NineWorlds/serenity-android.svg?branch=develop)](https://travis-ci.org/NineWorlds/serenity-android)


Serenity for Android is a client for the Plex Media Server developed by Plex.  
Serenity is not associated with Plex, Inc.   If you value the Plex Media Server,
consider donating to that project to help fund continued development.

Features
----

What does this support

* Browsing existing TV Show and Movie Libraries
* Playing back Movies and TV Shows
* Browsing by Genre for TV Shows and Movies
* Browsing by Season for TV Shows
* Download Videos for offline playback
* Tablets and Android TV devices (running 3.2 or higher)
* Playback videos from Queue
* Optional to playback movie trailers and episode previews from YouTube.
* Multiple views, from Grid View to Detail View.
* Auto discovery and works with all Plex 9 Media Servers, no server restriction.
* Second screen support including AllCast for Chromecasting your videos.
* Beta Music Library support.


What is currently not targeted:

* MyPlex access
* Photo Browsing
* Adding/Managing Channels and addins

With this said, this is an open source project, so if the community wants to contribute
code it will be welcomed.  Feature requests and bug reports can be opened on the issue
tracker.


Plexapp REST Library
----

This project includes a module that provides READ access to the plex media server REST API.
It can be use outside of the serenity application by others developers to access the metadata provided by a Plex Media Server.  The code is licensed under an MIT License.

What open source license is this using?
-----

The project is  using one of the more liberal open source licenses available. MIT.

http://opensource.org/licenses/MIT

Yes this means anybody can fork the project, and try to do their own client.  Commercial opportunities though
will be limited since the app will more than likely use the free Transcoder KEY that PlexApp provides.  Any
commercial resell of an app that uses the transcoder features of Plex needs a special key from Plex.  Thus
one of the reasons this project is open source and will probably stay free in the play store once it is
released there.


How can I help?
-----

If you are a programmer, fork the project, and provide patches or enhancements via pull requests.
If you don't have coding skills, but have graphic design skills, the project can always use a Logo, Icons, etc.
Otherwise, file bugs, and open enhancement requests.   I'm looking into various ways for donations to be sent
to help the project along as well.   The app will probably be free in the playstore.


Building from Source
=============

Make sure to set the ANDROID_HOME environement variable to the location where your SDK is deployed.

To build the APK and APKLibraries from the command line:

    ./gradlew clean assembleDebug

The application APK will be in serenity-app/build/outputs.  You can sideload this APK on your Android TV or Fire TV device.

Open Source Projects:
=====

This project uses several open source projects and source code:

* Android-Universal-ImageLoader (https://github.com/nostra13/Android-Universal-Image-Loader) - a nice library for managing image caches and downloading of images in the background.  Extremely fast response times both in the library and from the developer.
* Simple (http://simple.sourceforge.net/) - provides a nice interface to Serialize and Deserialize XML information.
* Dagger 1.2
* Retrofit 2
* OkHttp 3
* Android Priority Job Manager
* GreenRobot EventBus


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

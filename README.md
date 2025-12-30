Serenity for Android
=======================

[![Serenity Android CI](https://github.com/NineWorlds/serenity-android/actions/workflows/android.yml/badge.svg)](https://github.com/NineWorlds/serenity-android/actions/workflows/android.yml)[![codecov](https://codecov.io/gh/NineWorlds/serenity-android/branch/develop/graph/badge.svg)](https://codecov.io/gh/NineWorlds/serenity-android)

Plex Media Server support is now Deprecated!!!   Please consider using Emby Media Server instead if you want to use Serenity.

Serenity for Android is a client for the Emby Media Server and Jellyfin (in development), Plex Media Server support is no longer actively maintained.  
Serenity is not associated with Emby or Jellyfin.   If you value the Emby or Jellyfin Media Server,
consider donating to the respective projects to help fund continued development.

Features
----

What does this support

* Browsing existing TV Show and Movie Libraries
* Playing back Movies and TV Shows
* Browsing by Genre for TV Shows and Movies
* Browsing by Season for TV Shows
* Android TV and Fire TV devices running Android 4.1 or higher
* Playback videos from Queue (in development)
* Server discovery and works with all Emby Media Servers, no server restriction.

What is currently not targeted:

* Photo Browsing
* Live TV Channels
* Music

With this said, this is an open source project, so if the community wants to contribute
code it will be welcomed.  Feature requests and bug reports can be opened on the issue
tracker.

What open source license is this using?
-----

The project is  using one of the more liberal open source licenses available. MIT.

http://opensource.org/licenses/MIT

Yes this means anybody can fork the project, and try to do their own client.

How can I help?
-----

If you are a programmer, fork the project, and provide patches or enhancements via pull requests.
If you don't have coding skills, but have graphic design skills, the project can always use a Logo, Icons, etc.
Otherwise, file bugs, and open enhancement requests.   I'm looking into various ways for donations to be sent
to help the project along as well.   The app will probably be free in the Play Store.


Building from Source
=============

Make sure to set the ANDROID_HOME environment variable to the location where your SDK is deployed.

To build the APK and APKLibraries from the command line:

    ./gradlew clean assembleDebug

The application APK will be in serenity-app/build/outputs.  You can sideload this APK on your Android TV or Fire TV device.

Open Source Projects:
=====

This project uses several open source projects and source code:

* Simple (http://simple.sourceforge.net/) - provides a nice interface to Serialize and Deserialize XML information.
* Toothpick
* Retrofit 2
* OkHttp 3
* GreenRobot EventBus
* Glide v4 

Unit Testing Frameworks
------
* Robolectric (https://robolectric.org/) - Android Integration/Unit Testing framework that allows testing without launching an emulator.
* XMLUnit (http://xmlunit.sourceforge.net/) - unit testing framework to enhanced xml file verification.

Skins
====

Some layouts are influenced by the following skins.  Some icons reused from the Influence skin for XBMC

* Aeon
* MediaStreamer
* Influence

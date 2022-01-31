Serenity for Android
=======================

[![Build Status](https://circleci.com/gh/NineWorlds/serenity-android/tree/develop.svg?style=shield)](https://circleci.com/gh/NineWorlds/serenity-android/?branch=develop)[![codecov](https://codecov.io/gh/NineWorlds/serenity-android/branch/develop/graph/badge.svg)](https://codecov.io/gh/NineWorlds/serenity-android)
Sonar Qube:
[![SonarQube](https://sonarcloud.io/api/badges/measure?key=serenity-android:serenity-app&metric=coverage)](https://sonarcloud.io/dashboard?id=serenity-android%3Aserenity-app)

Plex Media Server support is now Deprecated!!!   Please consider using Emby Media Server instead if you want to use Serenity.

Serenity for Android is a client for the Emby Media Server, Plex Media Server support is no longer actively maintained.  
Serenity is not associated with Emby or Plex, Inc.   If you value the Emby or Plex Media Server,
consider donating to the respective project to help fund continued development.


Features
----

What does this support

* Browsing existing TV Show and Movie Libraries
* Playing back Movies and TV Shows
* Browsing by Genre for TV Shows and Movies
* Browsing by Season for TV Shows
* Android TV and Fire TV devices running Android 4.1 or higher
* Playback videos from Queue
* Optional to playback movie trailers and episode previews from YouTube.
* Multiple views, from Grid View to Detail View.
* Auto discovery and works with all Plex 9 Media Servers, no server restriction.

What is currently not targeted:

* Photo Browsing
* Live TV Channels
* Music

With this said, this is an open source project, so if the community wants to contribute
code it will be welcomed.  Feature requests and bug reports can be opened on the issue
tracker.


Plexapp REST Library (Deprecated)
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

* Simple (http://simple.sourceforge.net/) - provides a nice interface to Serialize and Deserialize XML information.
* Toothpick
* Retrofit 2
* OkHttp 3
* Android Priority Job Manager
* GreenRobot EventBus
* Glide v4 


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

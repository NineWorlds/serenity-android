Serenity for Android
=======================

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
* Transcoding
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

This requires that you have used the maven-android-sdk-deployer project to
install and deploy the Android 3.2 SDK to your maven repository. 

https://github.com/mosabua/maven-android-sdk-deployer

Make sure to set the ANDROID_HOME environement variable to the location where your SDK is deployed.

To build the APK and APKLibraries from the command line:

./mvnw clean install

The application APK will be in serenity-app/target.  You can sideload this APK on your Android TV or Tablet device.

Using Eclipse to Build:
===============

These instructions are from an adopter of the project.

1. Install Android SDK (was already done)
2. install maven 3.1.1 from https://maven.apache.org/download.cgi
3. set envorinmentvariables ANDROID_HOME and MAVEN_HOME and add $MAVEN_HOME/bin, $ANDROID_HOME/tools and $ANDROID_HOME/platform-tools to PATH
4. install eclipse plugin m2e from update site: http://www.eclipse.org/m2e/download/
5. install m2e-android from update site: http://rgladwell.github.com/m2e-android/updates/
6. install m2eclipse from marketplace (had to install the marketplace client before)
7. clone serenity repository
8. import existing maven project
9. update each project separatly over context menu Maven/Update project
10. copy project.properties from leftnavbarlibrary to menudrawer, edit the file in menudrawer-project and set target to android-17 to avoid NullPointerException during build
11. edit build-path for menudrawer and remove invalid system library and add your default jre-library
12. edit /serenity-app-tests/pom.xml and set robolectric to 2.2 according to my local repository
13. edit /serenity-app/pom.xml and change version for compatibility-v4 from 18 to 19.0.1 according to my local android sdk
14. select the error "Plugin execution not covered by lifecycle configuration...." in problem view and run the quick fix (ignore)
15. update all projects (maven / update projects) just to be sure.
16. Start serenity-app as android app and have fun. :-)

If you get errors because of @override-annotation, open project-settings, navigate to compiler-settings and set them from java 1.5 to 1.6


Open Source Projects:
=====

This project uses several open source projects and source code:

* LeftNavBarLibrary (http://code.google.com/p/googletv-android-samples/) from the google tv project.  This is packaged as a apklib for reuse.
* Android-Universal-ImageLoader (https://github.com/nostra13/Android-Universal-Image-Loader) - a nice library for managing image caches and downloading of images in the background.  Extremely fast response times both in the library and from the developer.
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


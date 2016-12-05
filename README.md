# Homecloud protocol library for Android
Java library implementing the Homecloud protocol for communication between Android apps and a webserver (such as [this one](https://github.com/HomeSkyLtd/server)).

## Organization
This repository consists of an Android Studio project with 2 sub-projects:
- homecloud-lib: This is an implementation of the Homecloud protocol in the form of a library, allowing it to be imported by any project that needs to communicate using this protocol;
- app: This is a debug app that exposes the API from the library, making it easier to debug the communication with the webserver.

## Usage
Just import this project on Android Studio. The sample app provided illustrates how to call the functions from the library.

# WebcamSnapshot
Simple Java application to take and export snapshots from a webcam and export them to another program.

This software uses sarxos' webcam-capture API: https://github.com/sarxos/webcam-capture

# Instructions
Upon first starting WebcamSnapshot it will create a wcss.properties file in the same directory if it doesn't already exist.

Properties used:
* application - Path to the application to launch. %1 can be used as a placeholder for the path to the temporary image file.

With WebcamSnapshot focused, pressing ENTER will start the default webcam driver and take a snapshot with the corresponding webcam. Pictures are always taken with the highest possible resolution and are saved to the temporary directory of your OS from where they will get deleted after closing WebcamSnapshot.
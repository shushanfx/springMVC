#!/bin/sh

cDir=`pwd`
logDir=/search/odin/resin/xmlviewer/log
echo "Change to $cDir..."
cd $cDir

if [ ! -d $logDir ] ; then
    mkdir -p $logDir
fi
echo "Start the tomcat server..."
nohup mvn jetty7:run -DspringMVC=true & 1>/search/odin/resin/xmlviewer/log/stdout.log 2>/search/odin/resin/xmlviewer/log/stderr.log

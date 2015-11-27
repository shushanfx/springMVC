#!/bin/sh

ps -ef | grep DspringMVC | awk '{print $2}' | while read line;
do
    echo "Kill process $line..."
    kill -9 $line
done
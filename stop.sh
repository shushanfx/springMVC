#!/bin/sh

ps -ef | grep DspringMVC | while read line;
do
    echo "Kill process $line..."
    kill -9 $line
done
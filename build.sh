#!/bin/bash

function run() {
    if [ "$1" == "build" ]; then
        action_build
    elif [ "$1" == "clean" ]; then
        action_clean
    elif [ "$1" == "run" ]; then
        action_run
    else
        echo "usage: "
        echo      ./build.sh build 
        echo      ./build.sh clean
        echo      ./build.sh run
    fi
}

function action_build() {
    javac src/Main.java -d class
    echo "javac completed with error code $?"
    echo
}

function action_clean() {
    rm class/*.class
    echo
}

function action_run() {
    java -classpath class Main
}


(run $@ && exit $?);





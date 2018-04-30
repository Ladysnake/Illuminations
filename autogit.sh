#!/bin/bash
if [ "$1" == "" ]
then
	read msg
else
	msg=$1
fi
git pull
git add *
git commit -m "$msg"
git push
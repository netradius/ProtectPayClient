#!/bin/bash

echo "Set the next RELEASE version"
mvn versions:set
mvn versions:commit
git commit pom.xml -m "Incrementing version"
mvn deploy
mvn scm:tag

echo "Set the next SNAPSHOT version"
mvn versions:set
mvn versions:commit
git commit pom.xml -m "Incrementing version"
git push

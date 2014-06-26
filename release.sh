#!/bin/bash

mvn versions:set
mvn versions:commit
git commit pom.xml -m "Incrementing version"
mvn deploy
mvn scm:tag

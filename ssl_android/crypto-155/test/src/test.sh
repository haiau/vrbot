#!/bin/bash

sh /Volumes/STICK/bcsign out/artifacts/hsm_jar/hsm.jar

java -cp ~/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.52/bcprov-jdk15on-1.52.jar:out/artifacts/hsm_jar/hsm.jar test.org.bouncycastle.test.TestRun


 

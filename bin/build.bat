@echo off
cd ../
mkdir target
cp src/main/resources/configuration_example.xml target/configuration.xml
mvn assembly:single

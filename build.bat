@echo off
cp src/main/resources/configuration_example.xml target/configuration.xml
mvn assembly:single

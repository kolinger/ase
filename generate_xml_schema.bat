@echo off
rm configuration.xsd
mvn org.codehaus.mojo:jaxb2-maven-plugin:schemagen

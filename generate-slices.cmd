@echo off
echo Generating slices...
slice2java --output-dir=src/main/java slices/Slices.ice
echo Done
pause
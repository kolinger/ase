@echo off
echo Generating slices...
slice2java --output-dir=src/main/java -Islices slices/model.ice slices/communication.ice
echo Done
pause
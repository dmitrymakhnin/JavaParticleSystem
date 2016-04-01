@echo off

if [%1]==[build] goto :build
if [%1]==[clean] goto :clean
if [%1]==[run]   goto :run

:usage
    @echo options:
    @echo     build: build the project.
    @echo     clean: clean the project.
    @echo     run:   run the project.
    @echo.
    @echo enter an option, none of the above to exit
    set /p option="> "
    
    if [%option%]==[build] goto build
    if [%option%]==[clean] goto clean
    if [%option%]==[run]   goto run
    
    goto :end

:build
    @echo building.
    javac src\Main.java -d class
    @echo javac completed with error code %ERRORLEVEL%
    goto :end

:clean
    @echo cleaning.
    del class\*.class    
    goto :end

:run
    @echo running.
    java -classpath class Main
    goto :end

:end
    pause

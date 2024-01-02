@REM This script executes commands to run up the needed containers stuff.
@REM Also, this script removes everything before up containers/builds.
@REM Useful for development.

cls
call docker-compose down --volumes --rmi all
call docker-compose up --build
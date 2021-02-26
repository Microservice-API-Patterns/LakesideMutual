@echo off

REM NPX will fail for users with spaces in their name, so we set the cache to the current folder
set npm_config_cache=%cd%

call npx recursive-install

REM All frontend projects installed successfully

IF %errorlevel% NEQ 0 ( 
  echo Aborting script, error %errorlevel%
  exit /b %errorlevel% 
)

call npx concurrently ^
  --kill-others ^
  --names "customer-core,customer-management-backend,customer-management-frontend" ^
  --prefix-colors "bgRed.bold,bgYellow.bold,bgBlue.bold" ^
  "cd customer-core && mvn spring-boot:run" ^
  "cd customer-management-backend && mvn spring-boot:run" ^
  "cd customer-management-frontend && npm start"

IF %errorlevel% NEQ 0 ( 
  echo Aborting script, error %errorlevel%
  exit /b %errorlevel% 
)
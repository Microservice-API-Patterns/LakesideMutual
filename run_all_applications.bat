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
  --names "eureka-server,customer-core,customer-self-service-backend,customer-self-service-frontend,customer-management-backend,customer-management-frontend,policy-management-backend,policy-management-frontend,risk-management-server,spring-boot-admin" ^
  --prefix-colors "bgRed.bold,bgYellow.bold,bgBlue.bold,bgBlue.bold,bgMagenta.bold,bgMagenta.bold,bgGreen.bold,bgGreen.bold,bgWhite.bold,bgRed.bold" ^
  "cd eureka-server && mvn spring-boot:run" ^
  "cd customer-core && mvn spring-boot:run" ^
  "cd customer-self-service-backend && mvn spring-boot:run" ^
  "cd customer-self-service-frontend && npm start" ^
  "cd customer-management-backend && mvn spring-boot:run" ^
  "cd customer-management-frontend && npm start" ^
  "cd policy-management-backend && mvn spring-boot:run" ^
  "cd policy-management-frontend && npm start" ^
  "cd risk-management-server && npm start" ^
  "cd spring-boot-admin && mvn spring-boot:run" 

IF %errorlevel% NEQ 0 ( 
  echo Aborting script, error %errorlevel%
  exit /b %errorlevel% 
)
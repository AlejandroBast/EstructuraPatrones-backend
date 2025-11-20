@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar
set WRAPPER_MAIN=org.apache.maven.wrapper.MavenWrapperMain
set JAVA_EXE=java.exe

where %JAVA_EXE% >nul 2>&1
if errorlevel 1 (
  echo Java no encontrado en PATH. Instala JDK 17+ y vuelve a intentar.
  exit /b 1
)

if not exist "%WRAPPER_JAR%" (
  echo maven-wrapper.jar no encontrado
  exit /b 1
)

set JAVA_TOOL_OPTIONS=%JAVA_TOOL_OPTIONS% --enable-native-access=ALL-UNNAMED

"%JAVA_EXE%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" -jar "%WRAPPER_JAR%" %*

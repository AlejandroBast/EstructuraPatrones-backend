#!/bin/sh
MAVEN_WRAPPER_DIR=".mvn/wrapper"
MAVEN_WRAPPER_JAR="$MAVEN_WRAPPER_DIR/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_WRAPPER_DIR/maven-wrapper.properties"

mkdir -p "$MAVEN_WRAPPER_DIR"

if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
  echo "Descargando maven-wrapper.jar..."
  curl -fSL "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" -o "$MAVEN_WRAPPER_JAR" || {
    echo "Error descargando maven-wrapper.jar"; exit 1; }
fi

if [ ! -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
  echo "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip" > "$MAVEN_WRAPPER_PROPERTIES"
fi

JAVA_EXE="java"
command -v $JAVA_EXE >/dev/null 2>&1 || { echo "Java no encontrado en PATH. Instala JDK 17+"; exit 1; }

exec "$JAVA_EXE" -classpath "$MAVEN_WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"

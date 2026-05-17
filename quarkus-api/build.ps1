# build.ps1
# 1. Fuerza la variable JAVA_HOME al JDK 25 local
$env:JAVA_HOME="C:\Program Files\Java\jdk-25.0.3+9"

# 2. Ejecuta Maven pasando todos los argumentos que le des al script
.\mvnw $args
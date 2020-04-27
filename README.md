### Build:

1. Find **Siebel.jar** in Siebel Application Servers's %SIEBSRVR_ROOT%\CLASSES\ folder
2. Copy file to local machine
3. Execute command: `mvnw install:install-file -Dfile=<path-to-Siebel.jar> -DgroupId=com.siebel -DartifactId=siebel-jar -Dversion=1.0.0 -Dpackaging=jar`
4. Execute command: `mvnw clean package`

### Configuration:

1. Import **result/EAI JSON Converter.sif** into repository
2. Put **result/siebel-json.jar** into %SIEBSRVR_ROOT%\CLASSES\
3. Configure server

Enterprise profile:

| Profile | Alias | Subsystem Type |
| :------ |:----- | :------------- |
| JAVA    | JAVA  | JVMSubSys      |

Profile parameters:

| Name          | Alias     | Data Type | Value                                                                                                                                                                                                                                                                                   |
| :------------ | :-------- | :-------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| JVM Classpath | CLASSPATH | String    | %SIEBSRVR_ROOT%\CLASSES\Siebel.jar;%SIEBSRVR_ROOT%\CLASSES\siebel-json.jar                                                                                                                                                                                                              |
| JVM DLL Name  | DLL       | String    | Windows:<br>C:\Program Files (x86)\Java\jre1.8.0_211\bin\client\jvm.dll<br><br>CentOS:<br>/usr/java/jdk1.8.0_202-i586/jre/lib/i386/server/libjvm.so<br>Server's bash `export LD_LIBRARY_PATH=/usr/java/jdk1.8.0_202-i586/jre/lib/i386/server: /usr/java/jdk1.8.0_202-i586/jre/lib/i386` |
| JVM Options   | VMOPTIONS | String    | -Xms512m -Xmx512m                                                                                                                                                                                                                                                                       |

For debug use:

| Name          | Alias     | Data Type | Value                                                                                 |
| :------------ | :-------- | :-------- | :------------------------------------------------------------------------------------ |
| JVM Classpath | CLASSPATH | String    | ...                                                                                   |
| JVM DLL Name  | DLL       | String    | ...                                                                                   |
| JVM Options   | VMOPTIONS | String    | -Xms512m -Xmx512m  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7896 |

4. Log files are located in %SIEBSRVR_ROOT%\BIN\siebel-json-logs\
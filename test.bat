@echo off

set log4j=properties/log4j2.properties
set properties=properties/environment.properties

java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar csv add_address "Moscow,Test,Test"

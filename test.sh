#!/bin/sh

LOG4J2="properties/log4j2.properties"
PROPERTIES="properties/config.properties"

java -Dlog4j2.configurationFile=$LOG4J2 -Dconfig=$PROPERTIES -jar deals.jar xml add_address "Moscow,Moscow region,Central"

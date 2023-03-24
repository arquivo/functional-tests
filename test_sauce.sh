#!/bin/bash
#
# Script to simplify local testing. 
#    Requires defining environment variable SC_BIN with the location of sauce_connect binary
#    Requires defining environment variables SAUCE_USERNAME SAUCE_ACCESS_KEY
# Usage:   ./test <test class(es)>
# Examples:
#     Run every test under the namespace pt.arquivo.tests.webapp.replay.menu:
#     ./test pt.arquivo.tests.webapp.replay.menu.**  
#     Run the datepicker test:
#     ./test pt.arquivo.tests.webapp.datepicker.DatePickerTest
#     Run the datepicker test and the memorial test:
#     ./test pt.arquivo.tests.webapp.datepicker.DatePickerTest,pt.arquivo.tests.memorial.MemorialTest

WIN_CHROME='{"platformName":"Windows 10", "browserName":"Chrome", "browserVersion":"latest", "screenResolution":"1280x960"}'
WIN_FIREFOX='{"platformName":"Windows 10", "browserName":"Firefox", "browserVersion":"latest", "screenResolution":"1280x960"}'
WIN_EDGE='{"platformName":"Windows 10", "browserName":"Edge", "browserVersion":"latest", "screenResolution":"1280x960"}'
MAC_CHROME='{"platformName":"MacOS", "browserName":"Chrome", "browserVersion":"latest", "screenResolution":"1280x960"}'
MAC_FIREFOX='{"platformName":"MacOS", "browserName":"Firefox", "browserVersion":"latest", "screenResolution":"1280x960"}'
MAC_SAFARI='{"platformName":"MacOS", "browserName":"Safari", "browserVersion":"latest", "screenResolution":"1280x960"}'
AND_CHROME='{"platformName":"Android", "platformVersion":"12.0", "browserName":"Chrome", "device":"Android GoogleAPI Emulator", "automationName":"UIAutomator2"}'
IOS_SAFARI='{"platformName":"iOS", "platformVersion":"15.4", "browserName":"Safari", "device":"iPhone Simulator", "automationName":"XCUITest"}'

WINDOWS="$WIN_CHROME , $WIN_FIREFOX , $WIN_EDGE"
MAC="$MAC_CHROME , $MAC_FIREFOX , $MAC_SAFARI"
ANDROID="$AND_CHROME"
IOS="$IOS_SAFARI"

DESKTOP="$WINDOWS , $MAC"
MOBILE="$ANDROID , $IOS"

#Edit this to change test devices:
export SAUCE_ONDEMAND_BROWSERS="[ $WIN_CHROME , $MOBILE ]"

export TUNNEL_NAME="teste_sauce"
export SAUCE_USERNAME="xxx"
export SAUCE_ACCESS_KEY="xxxxxx-xxxxx-xxxxx"

export SELENIUM_HOST=localhost
export SELENIUM_PORT=4444


"$SC_BIN" --user "$SAUCE_USERNAME" --api-key "$SAUCE_ACCESS_KEY" -P "$SELENIUM_PORT" --tunnel-name "$TUNNEL_NAME" &

mvn clean verify \
   -Djava.locale.providers=COMPAT,SPI \
   -Dtest.build.id="teste_sauce" \
   -Dtest.url=https://preprod.arquivo.pt \
   -Dit.test=$1

pkill -f "$TUNNEL_NAME"
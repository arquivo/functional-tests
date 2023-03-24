#!/bin/bash

set -e

while ! curl -sSL "http://localhost:4444/wd/hub/status" 2>&1 \
        | jq -r '.value.ready' 2>&1 | grep "true" >/dev/null; do
    echo 'Waiting for the Grid'
    sleep 1
done


browsers='[{"browserName": "Chrome", "browserVersion": "105.5", "platform": "Linux"}]'

>&2 echo "Selenium Grid is up - executing tests"
mvn clean verify \
   -Djava.locale.providers=COMPAT,SPI \
   -Dtest.url="$1" \
   -Dit.test="$2" \
   -Dtest.browsers.json="$browsers" \
   -Dtest.selenium.port=4444 \
   -Dtest.selenium.host=localhost
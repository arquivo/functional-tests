# Recursive tests developed with selenium framework for Arquivo.pt
---------------

## Status of functional tests build

The link below must be updated for each build:

[![Build Status](https://app.saucelabs.com/browser-matrix/ArquivoPT.svg)](https://app.saucelabs.com/builds/5ed5b031f86a379db868ece6ad5f9711)

## Run Functional Tests

Generate eclipse .project and .classpath files, similar for idea.

```bash
 mvn eclipse:clean eclipse:eclipse
```

Execute the tests

```bash
 mvn clean verify -Dit.test=pt.fccn.arquivo.tests.AllTests -Dtest.url=https://preprod.arquivo.pt -Dremote.saucelabs.user=xxxx -Dremote.saucelabs.key=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx -Dtest.resolution=1280x1024 -Djava.locale.providers=COMPAT,SPI
```

To debug tests add the argument:

```bash
-Dmaven.failsafe.debug
```

To run the test collection that don't use saucelabs run:

```bash
 mvn clean verify -Dit.test=pt.fccn.arquivo.tests.TestCollections
```

Additionaly the maven has a couple of profiles to make it more easilly to run tests directly to a specific browser.
For Internet Explorer:

```bash
-Dbrowsers=IE
```

For mobile phones:

```bash
-Dbrowsers=mobile
```

For development purposes you could use:

```bash
-Dbrowsers=desktop-linux
```

## Speed up development

Use sauce labs directly for development of this tests could take too much time. Another aproach is to replace sauce labs with a local selenium grid.

On a different shell start selenium grid using:

```bash
docker-compose up
```

And another run your tests like:

```bash
mvn clean verify -Dbrowsers=desktop-linux -Dit.test=pt.fccn.arquivo.tests.imagesearch.ImageSearchTest -Dtest.url=https://arquivo.pt -Djava.locale.providers=COMPAT,SPI
```

More information on:
https://github.com/SeleniumHQ/docker-selenium

## Kill process

In some cases when the test or tunnel is closed incorrectly a process is pending which makes it impossible to run the test again. So, use the following steps:

```bash
ps aux | grep sc
```
And a process will appear with a name similar to "/root/sc-4.4.12-linux/bin/sc -u ArquivoPT". Then,

```bash
kill process_ID
```

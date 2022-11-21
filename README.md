Recursive tests developed with selenium framework for Arquivo.pt
---------------

[![Build Status](https://app.saucelabs.com/browser-matrix/ArquivoPT.svg)](https://app.saucelabs.com/builds/7cf39791df3937efa655426a7768c0bb)

# Functional Tests

To select which platform/browser combinations to test on, set the SAUCE_ONDEMAND_BROWSERS environment variable to a string such as:

```bash
[
  {
    "platform": "Windows 10",
    "browser": "Chrome",
    "browser-version": "latest",
    "resolution": "1280x960"
  },
  {
    "platform":"Android",
    "browser":"Chrome",
    "browser-version":"12.0",
    "device": "Android GoogleAPI Emulator",
    "automation-name": "UiAutomator2"
  }
]
```

## Execute the tests

```bash
 mvn clean verify -Dit.test=pt.arquivo.tests.** -Dtest.url=https://preprod.arquivo.pt -Dremote.saucelabs.user=xxxx -Dremote.saucelabs.key=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx -Djava.locale.providers=COMPAT,SPI -Dtest.selenium.host=ondemand.us-west-1.saucelabs.com -Dtest.selenium.port=443
```

To debug tests add the argument:

```bash
-Dmaven.failsafe.debug
```

## To execute through sauce-connect proxy:

First start the proxy:
```bash
/root/sc-4.4.12-linux/bin/sc --user xxxx --api-key xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx --se-port 4444 
```

```bash
 mvn clean verify -Dit.test=pt.arquivo.tests.** -Dtest.url=https://preprod.arquivo.pt -Dremote.saucelabs.user=xxxx -Dremote.saucelabs.key=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx -Djava.locale.providers=COMPAT,SPI -Dtest.selenium.host=localhost -Dtest.selenium.port=4444
```

In some cases when the test or tunnel is closed incorrectly a process is pending which makes it impossible to run the test again. So, use the following steps:

```bash
ps aux | grep sc
```
And a process will appear with a name similar to "/root/sc-4.4.12-linux/bin/sc -u ArquivoPT". Then,

```bash
kill process_ID
```


## Speed up development

Use sauce labs directly for development of this tests could take too much time. Another aproach is to replace sauce labs with a local selenium grid.

On a different shell start selenium grid using:

```bash
docker-compose up
```

And another run your tests like:

```bash
mvn clean verify -Dit.test=pt.fccn.arquivo.tests.imagesearch.ImageSearchTest -Dtest.url=https://arquivo.pt -Djava.locale.providers=COMPAT,SPI -Dtest.selenium.port=4444 -Dtest.selenium.host=localhost
```

More information on:
https://github.com/SeleniumHQ/docker-selenium


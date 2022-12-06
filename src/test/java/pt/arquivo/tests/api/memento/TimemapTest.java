package pt.arquivo.tests.api.memento;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import pt.arquivo.utils.AppendableErrorsBaseTest;

/**
 *
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class TimemapTest extends AppendableErrorsBaseTest {

    private String testURL;

    public TimemapTest() {
        this.testURL = System.getProperty("test.url");
    }

    @Test
    public void timemapTest() {

        String timemapStr = this.testURL + "/wayback/timemap/link/www.fccn.pt";
        URL url;
        try {
            url = new URL(timemapStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Error generating URL to a timemap of a page verify if you have passed correct test url parameter",
                    e);
        }
        byte[] timemapBytes;
        try {
            InputStream is = url.openStream();
            timemapBytes = IOUtils.toByteArray(is);

        } catch (IOException e) {
            throw new RuntimeException("Error downloading the timemap", e);
        }

        String timemap = new String(timemapBytes);
        String[] lines = timemap.split(System.getProperty("line.separator"));

        boolean existSelf = false;
        boolean existTimegate = false;
        boolean existOriginal = false;
        boolean existMemento = false;

        for (int i = 0; i < lines.length; i++) {
            String[] line = lines[i].split(";");
            if (lines[i].contains("rel=\"self\"")) {
                assertThat("Check url self", line[0],
                        containsString("<" + this.testURL + "/wayback/timemap/link/http://www.fccn.pt"));
                assertThat("Check rel self", line[1], containsString("rel=\"self\""));
                assertThat("Check type self", line[2], containsString("type=\"application/link-format\""));
                assertThat("Check from self", line[3], containsString("from=\"Sun, 13 Oct 1996 14:56:50 GMT\""));
                existSelf = true;
            } else if (lines[i].contains("rel=\"timegate\"")) {
                assertThat("Check url timegate", line[0],
                        containsString("<" + this.testURL + "/wayback/http://www.fccn.pt>"));
                assertThat("Check rel timegate", line[1], containsString("rel=\"timegate\""));
                existTimegate = true;
            } else if (lines[i].contains("rel=\"original\"")) {
                assertThat("Check url original", line[0], containsString("<http://www.fccn.pt>"));
                assertThat("Check rel original", line[1], containsString("rel=\"original\""));
                existOriginal = true;
            } else if (lines[i].contains("rel=\"memento\"")) {
                assertThat("Check if exists fccn.pt on url memento", line[0], containsString("fccn.pt"));
                assertThat("Check if exists " + this.testURL + " on memento", line[0],
                        containsString("<" + this.testURL));
                assertThat("Check first rel memento", line[1], containsString("rel=\"memento\""));
                assertThat("Check first datetime memento", line[2], containsString("datetime=\""));
                if (lines[i].contains("19961013145650mp_")) {
                    assertThat("Check first url memento", line[0].replace(":80", ""),
                            containsString("<" + this.testURL + "/wayback/19961013145650mp_/http://www.fccn.pt/>"));
                    assertThat("Check first datetime memento", line[2],
                            containsString("datetime=\"Sun, 13 Oct 1996 14:56:50 GMT\""));
                }
                existMemento = true;
            } else {
                throw new IllegalStateException("Missing element(s) \"rel=*\"");
            }

        }
        assertEquals("There is no parameter Self", existSelf, true);
        assertEquals("There is no parameter Timegate", existTimegate, true);
        assertEquals("There is no parameter Original", existOriginal, true);
        assertEquals("There is no parameter Memento", existMemento, true);
    }

}

package akyan.nlp.news.helpers;

import org.junit.Test;

import static akyan.nlp.news.helpers.RegexUtil.preg_match_all;
import static akyan.nlp.news.helpers.RegexUtil.preg_quote;
import static akyan.nlp.news.helpers.RegexUtil.str_ireplace;
import static org.junit.Assert.assertEquals;

/**
 * Created by yusuf on 08/11/16.
 */
public class RegexUtilTest {

    public RegexUtilTest() {
        org.apache.log4j.BasicConfigurator.configure();
    }

    @Test
    public void preg_match_all_test() {
        // regex in word boundary and not case sensitive
        int result1 = preg_match_all("KOTABARU", "(?i)\\bbaru\\b");
        assertEquals(0, result1);

        // regex in word boundary and not case sensitive
        int result2 = preg_match_all("KOTABARU", "(?i)\\bkotabaru\\b");
        assertEquals(1, result2);
    }

    @Test
    public void preg_quote_test() {
        // TIRO/TRUSEB (id = 1109090) is a valid name for district name in  Pidie Regency, Aceh
        String pregquoted = preg_quote("TIRO/TRUSEB");
        assertEquals("TIRO/TRUSEB", pregquoted);
    }

    @Test
    public void str_ireplace_test() {
        String replaced = str_ireplace(new String[]{"kabupaten"}, "kota", "kabupaten yogyakarta");
        assertEquals("kota yogyakarta", replaced);
    }
}

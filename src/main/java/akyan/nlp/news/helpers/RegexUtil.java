package akyan.nlp.news.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yusuf on 06/11/16.
 */
public class RegexUtil {

    public static int preg_match_all(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        int count = 0;
        while (matcher.find()) { count++; }

        return count;
    }

    public static String preg_quote(String pStr) {
        return pStr.replaceAll("[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]", "\\\\$0");
    }

    public static String str_ireplace(String[] exclude, String replacement, String str) {
        String newString = str.trim();
        for(String ex : exclude) {
            newString = newString.replaceAll("(?i)\\b" + preg_quote(ex) + "\\b", replacement);
        }

        return newString.trim();
    }

    public static String str_replace(String[] exclude, String replacement, String str) {
        String newString = str.trim();
        for(String ex : exclude) {
            newString = newString.replaceAll("\\b" + preg_quote(ex) + "\\b", replacement);
        }

        return newString.trim();
    }
}

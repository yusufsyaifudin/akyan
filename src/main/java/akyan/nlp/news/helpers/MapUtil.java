package akyan.nlp.news.helpers;

import java.util.*;

/**
 * Created by yusuf on 10/11/16.
 */
public class MapUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByKeyStringLength( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort( list, (o1, o2) -> {
            String k1s = (String) o1.getKey();
            Integer k1 = k1s.length();

            String k2s = (String) o2.getKey();
            Integer k2 = k2s.length();
            return k2.compareTo(k1);
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}

package ScalaForTheImpatient;

import java.util.Map;

public class Chapter04MapsAndTuplesSupport {

    public final StringBuilder log = new StringBuilder();

    public void needAJavaMap(Map<String, ?> attributes) {
        for(String key : attributes.keySet()) {
            Object value = attributes.get(key);
            if (log.length() > 0) {
                log.append(",");
            }
            log.append("(").append(key).append(":").append(value).append(")");
        }
    }
}

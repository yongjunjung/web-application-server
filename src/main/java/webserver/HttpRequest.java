package webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yongjunjung on 2017. 4. 12..
 */
public class HttpRequest {

    private Map<String, String> headerMap = new HashMap<String, String>();


    public String getHeader(String key) {
        return headerMap.get(key);
    }

    public String getRequestPath(String line) {
        String[] tokens = line.split(" ");
        return tokens[1];
    }

}

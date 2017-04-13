package webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yongjunjung on 2017. 4. 13..
 */
public class HttpHeaders {

    private Map<String, String> headers = new HashMap<String, String>();

    public void add(String headerLine) {
        String[] headers = headerLine.split(": ");
        this.headers.put(headers[0], headers[1]);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public int getContentLength() {

        String contentLength = headers.get("Content-Length");
        if (contentLength == null) {
            return 0;
        }

        return Integer.parseInt(contentLength);
    }
}

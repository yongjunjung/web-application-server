package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yongjunjung on 2017. 4. 12..
 */
public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private Map<String, String> headerMap = new HashMap<String, String>();

    public void addResponseHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public void sendRedirectUrl(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " +url+"\n \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}

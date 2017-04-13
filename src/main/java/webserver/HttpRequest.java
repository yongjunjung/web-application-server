package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yongjunjung on 2017. 4. 12..
 */
public class HttpRequest {

    private String method;
    private String path;

    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    BufferedReader br;
    RequestLine requestLine;

    public HttpRequest() {
    }

    public HttpRequest(InputStream is) {

        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();
            if (line == null) {
                return ;
            }

            requestLine = new RequestLine(line);
            this.params = requestLine.getParams();

            processHeader();

            String contentLengh = headers.get("Content-Length");



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processHeader() throws IOException {
        String line;
        while (!"".equals(line = br.readLine())) {
            String[] headers = line.split(": ");
            this.headers.put(headers[0], headers[1]);
        }
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
     }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getRequestPath(String line) {
        String[] tokens = line.split(" ");
        return tokens[1];
    }

}

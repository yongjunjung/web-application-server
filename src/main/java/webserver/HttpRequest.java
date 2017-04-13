package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yongjunjung on 2017. 4. 12..
 */
public class HttpRequest {

    private String method;
    private String path;

    private Map<String, String> headers = new HashMap<String, String>();

    BufferedReader br;


    public HttpRequest() {
    }

    public HttpRequest(InputStream is) {

        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            if (line == null) {
                return ;
            }

            String[] tokens = line.split(" ");
            this.path = tokens[0];

            String[] urls = tokens[1].split("\\?");
            this.path = urls[0];

            while (!"".equals(line = br.readLine())) {
                String[] headers = line.split(": ");
                this.headers.put(headers[0], headers[1]);
            }

            String contentLengh = headers.get("Content-Length");



        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
     }


    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getRequestPath(String line) {
        String[] tokens = line.split(" ");
        return tokens[1];
    }

}

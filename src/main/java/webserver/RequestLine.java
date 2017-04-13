package webserver;

import util.HttpRequestUtils;

import java.util.Map;

/**
 * Created by yongjunjung on 2017. 4. 13..
 */
public class RequestLine {


    private String method;
    private String path;
    private Map<String, String> params;

    public RequestLine(String line) {
        String[] tokens = line.split(" ");
        this.method = tokens[0];

        String[] urls = tokens[1].split("\\?");
        this.path = urls[0];

        if (urls.length == 2) {
            this.params = HttpRequestUtils.parseQueryString(urls[1]);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }
}

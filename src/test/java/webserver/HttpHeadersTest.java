package webserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yongjunjung on 2017. 4. 13..
 */
public class HttpHeadersTest {

    @Test
     public void add() throws Exception {

         //1.Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Connection: keep-alive");
        assertEquals("keep-alive", headers.getHeader("Connection"));
     }

    @Test
    public void contentLength() throws Exception {

        //1.Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Length: 49");
        assertEquals(49, headers.getContentLength());
    }
}
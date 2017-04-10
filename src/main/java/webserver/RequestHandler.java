package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            if (line == null) {
                return ;
            }

            String path = getRequestPath(line);

            Map<String, String> headerMap = new HashMap<String, String>();

            while (!"".equals(line)) {
                line = br.readLine();
                String[] headers = line.split(": ");
                if (headers.length == 2) {
                    headerMap.put(headers[0], headers[1]);
                }
            }

            if ("/user/create".equals(path)) {
                log.debug("회원가입");
                String bodyData = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")) + 1);
                Map<String, String> paramMap = parseQueryString(bodyData);
                User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
                log.debug("user={}", user.toString());

                DataBase.addUser(user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);

            } else if ("/user/login".equals(path)) {
                log.debug("회원로그인");
                String bodyData = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")) + 1);
                Map<String, String> paramMap = parseQueryString(bodyData);
                String userId = paramMap.get("userId");
                String password = paramMap.get("password");

                User user = DataBase.findUserById(userId);
                if (user == null || !password.equals(user.getPassword())) {
                    log.debug("로그인 실패");
                    DataOutputStream dos = new DataOutputStream(out);
                    responseLoginFail(dos);
                } else {
                    log.debug("로그인 성공");
                    DataOutputStream dos = new DataOutputStream(out);
                    response302LoginSuccessHeader(dos);
                }

            }else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getRequestPath(String line) {
        String[] tokens = line.split(" ");
        return tokens[1];
    }

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: /index.html\n \r\n");
            dos.writeBytes("Set-Cookie : logined=true; Path=/ \n \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseLoginFail(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: /user/login_failed.html\n \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html\n \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

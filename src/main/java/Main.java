import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
public class Main {
    static String CRLF = "\r\n";
    public static void main(String[] args) {
            try (ServerSocket serverSocket = new ServerSocket(4221)) {
                while (true) {
                    serverSocket.setReuseAddress(true);
                    Socket clientSocket = serverSocket.accept();
                    clientHandler CH = new clientHandler(clientSocket);
                    CH.run();
                    Thread thread = new Thread(CH);
                    thread.start();
//                OutputStream clientOutput = clientSocket.getOutputStream();
//                InputStream clientInput = clientSocket.getInputStream();
//                BufferedWriter bufferedWriter =
//                        new BufferedWriter(new OutputStreamWriter(clientOutput));
//                BufferedReader bufferedReader =
//                        new BufferedReader(new InputStreamReader(clientInput));
//                String firstLine = bufferedReader.readLine();
//                String path = firstLine.split(" ")[1];
//                if (path.equals("/")) {
//                    bufferedWriter.write("HTTP/1.1 200 OK" + CRLF + CRLF);
//                } else if (path.split("/")[1].equals("echo")) {
//                    String txt = path.split("/")[2];
//                    String response =
//                            "HTTP/1.1 200 OK" + CRLF + "Content-Type: text/plain" + CRLF +
//                                    "Content-Length: " + txt.length() + CRLF + CRLF + txt;
//                    bufferedWriter.write(response);
//                } else if (path.split("/")[1].equals("user-agent")) {
//                    String host = bufferedReader.readLine();
//                    String agentLine = bufferedReader.readLine();
//                    if (agentLine.split(" ")[0].equals("User-Agent:")) {
//                        String content = agentLine.split(" ")[1];
//                        String response =
//                                "HTTP/1.1 200 OK" + CRLF + "Content-Type: text/plain" + CRLF +
//                                        "Content-Length: " + content.length() + CRLF + CRLF + content;
//                        bufferedWriter.write(response);
//                    }
//                } else {
//                    bufferedWriter.write("HTTP/1.1 404 NOT FOUND" + CRLF + CRLF);
//                }
//                bufferedWriter.close();
            }
        } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
    }
}
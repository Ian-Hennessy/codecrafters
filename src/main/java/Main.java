import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
    static String CRLF = "\r\n";

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        //System.out.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage

        //       serverSocket = new ServerSocket(4221);
//       serverSocket.setReuseAddress(true);
//       clientSocket = serverSocket.accept(); // Wait for connection from client.
//       System.out.println("accepted new connection");
        try (ServerSocket serverSocket = new ServerSocket(4221)) {
            try (Socket clientSocket = serverSocket.accept()) {
                OutputStream clientOutput = clientSocket.getOutputStream();
                InputStream clientInput = clientSocket.getInputStream();
                BufferedWriter bufferedWriter =
                        new BufferedWriter(new OutputStreamWriter(clientOutput));
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(clientInput));
                String firstLine = bufferedReader.readLine();
                String path = firstLine.split(" ")[1];
                if (path == "/") {
                    bufferedWriter.write("HTTP/1.1 OK 200" + CRLF + CRLF);
                } else if (path.split("/")[0].equals("echo")) {
                        String txt = path.split("/")[2];
                        String response =
                                "HTTP/1.1 200 OK" + CRLF + "Content-Type: text/plain" +
                                        CRLF + "Content-Length: " + txt.length() +
                                        CRLF + CRLF + txt;
                        bufferedWriter.write(response);
                    } else {
                    bufferedWriter.write("HTTP/1.1 404 NOT FOUND" + CRLF + CRLF);
                }
                bufferedWriter.close();
            } catch (IOException e) {
                System.out.println("IOException:" + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

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

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        //       serverSocket = new ServerSocket(4221);
//       serverSocket.setReuseAddress(true);
//       clientSocket = serverSocket.accept(); // Wait for connection from client.
//       System.out.println("accepted new connection");
        try {
            serverSocket = new ServerSocket(4221);
            try {
                clientSocket = serverSocket.accept();
                OutputStream clientOutput = clientSocket.getOutputStream();
                InputStream clientInput = clientSocket.getInputStream();
                BufferedWriter bufferedWriter =
                        new BufferedWriter(new OutputStreamWriter(clientOutput));
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(clientInput));
                String firstLine = bufferedReader.readLine();
                String path = firstLine.split(" ")[1];
                if (path == "/") {
                    bufferedWriter.write("HTTP/1.1 200 OK" + CRLF + CRLF);
                } else {
                    bufferedWriter.write("HTTP/1.1 404 NOT FOUND" + CRLF + CRLF);
                }
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                System.out.println("IOException:" + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

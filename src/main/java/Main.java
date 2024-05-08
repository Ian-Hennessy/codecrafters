import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static String CRLF = "\r\n";

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        //System.out.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
//       serverSocket = new ServerSocket(4221);
//       serverSocket.setReuseAddress(true);
//       clientSocket = serverSocket.accept(); // Wait for connection from client.
//       System.out.println("accepted new connection");
            try {
                serverSocket = new ServerSocket(4221);
                try {
                    clientSocket = serverSocket.accept();
                    OutputStream clientOutput = clientSocket.getOutputStream();
                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new OutputStreamWriter(clientOutput));
                    bufferedWriter.write("HTTP/1.1 200 OK" + CRLF + CRLF);
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println("IOExceltion:" + e.getMessage());
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

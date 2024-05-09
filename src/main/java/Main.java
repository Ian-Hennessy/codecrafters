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
                    ClientHandler CH = new ClientHandler(clientSocket);
                    Thread thread = new Thread(CH);
                    thread.start();

            }
        } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
    }
}
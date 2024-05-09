import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    static String CRLF = "\r\n";
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try {
            processRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void processRequest() throws IOException {
        InputStream clientInputStream = clientSocket.getInputStream();
        OutputStream clientOutputStream = clientSocket.getOutputStream();
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(clientInputStream));
        BufferedWriter bufferedWriter =
                new BufferedWriter(new OutputStreamWriter(clientOutputStream));
        String requestFirstLine = bufferedReader.readLine();
        String path = requestFirstLine.split(" ")[1];
        if (path.equals("/")) {
            clientOutputStream.write(("HTTP/1.1 200 OK" + CRLF + CRLF).getBytes());
        } else if (path.equals("/user-agent")) {
            String headerPair;
            while ((headerPair = bufferedReader.readLine()) != null) {
                String[] splits = headerPair.split(":");
                String headerKey = splits[0];
                String headerValue = splits[1].strip();
                if (headerKey.equals("User-Agent")) {
                    String response = "HTTP/1.1 200 OK" + CRLF +
                            "Content-Type: text/plain" + CRLF +
                            "Content-Length: " + headerValue.length() + CRLF +
                            CRLF + headerValue;
                    System.out.println(response);
                    // write bytes not UTF characters !
                    clientOutputStream.write(
                            response.getBytes(StandardCharsets.US_ASCII));
                }
            }
        } else if (path.split("/")[1].equals("echo")) {
            String echoMe = path.split("/")[2];
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Type: text/plain" +
                    CRLF + "Content-Length: " + echoMe.length() + CRLF +
                    CRLF + echoMe;
            bufferedWriter.write(response);
        } else {
            bufferedWriter.write("HTTP/1.1 404 NOT FOUND" + CRLF + CRLF);
        }
        bufferedWriter.close();
    }
}







//import java.io.*;
//import java.net.Socket;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.nio.charset.StandardCharsets;
//
//public class clientHandler implements Runnable {
//    private final Socket clientSocket;
//    private final String CRLF = "\r\n";
//    public clientHandler (Socket client) {
//        this.clientSocket = client;
//    }
//
//    @Override
//    public void run() {
//        try {
//            process();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void process() throws IOException {
//        InputStream clientInputStream = clientSocket.getInputStream();
//        OutputStream clientOutputStream = clientSocket.getOutputStream();
//        BufferedReader clientReader =
//                new BufferedReader(new InputStreamReader(clientInputStream));
//        BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientOutputStream));
//
//        String firstLine = clientReader.readLine();
//        String path = firstLine.split(" ")[1];
//
//        if (path.equals("/")) {
//            String response = "HTTP/1.1 200 OK" + CRLF + CRLF;
//            clientOutputStream.write(response.getBytes());
//        } else if (path.split("/")[0].equals("echo")) {
//            String txt = path.split("/")[1];
//            String response = "HTTP/1.1 200 OK" + CRLF +
//                    "Content-Type: text/plain" + CRLF +
//                    "Content-Length: " + txt.length() + CRLF + CRLF + txt;
//            clientWriter.write(response);
//        } else if (path.split("/")[1].equals("user-agent")) {
//            String headerPair = clientReader.readLine();
//            while (headerPair != null) {
//                String[] split = headerPair.split(":");
//                String val = split[1].strip();
//                if (split[0].equals("User-Agent")) {
//                    String response = "HTTP/1.1 200 OK" + CRLF +
//                            "Content-Type: text/plain" + CRLF +
//                            "Content-Length: " + val.length() +
//                            CRLF + CRLF + val;
//                    System.out.println(response);
//                    clientOutputStream.write(response.getBytes(StandardCharsets.US_ASCII));
//                }
//                headerPair = clientReader.readLine();
//            }
//        } else {
//            clientWriter.write("HTTP/1.1 404 NOT FOUND" + CRLF + CRLF);
//        }
//        clientWriter.close();
//    }
//}
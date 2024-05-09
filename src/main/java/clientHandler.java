import java.io.*;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class clientHandler implements Runnable {
    private final Socket clientSocket;
    private final String CRLF = "\r\n";
    public clientHandler (Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try {
            process();
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

    private void process() throws IOException {
        InputStream clientInputStream = clientSocket.getInputStream();
        OutputStream clientOutputStream = clientSocket.getOutputStream();
        BufferedReader clientReader =
                new BufferedReader(new InputStreamReader(clientInputStream));
        BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientOutputStream));

        String firstLine = clientReader.readLine();
        String path = firstLine.split(" ")[1];
        String command = path.split("/")[0];
        switch (path) {
            case "/" -> {
                String response = "HTTP/1.1 200 OK" + CRLF + CRLF;
                clientOutputStream.write(response.getBytes());
            }
            case "/echo" -> {
                String txt = path.split("/")[1];
                String response = "HTTP/1.1 200 OK" + CRLF +
                        "Content-Type: text/plain" + CRLF +
                        "Content-Length: " + txt.length() + CRLF + CRLF + txt;
                clientWriter.write(response);
            }
            case "/user-agent" -> {
                String headerPair = clientReader.readLine();
                while (headerPair != null) {
                    String[] split = headerPair.split(":");
                    String val = split[1].strip();
                    if (split[0].equals("User-Agent")) {
                        String response = "HTTP/1.1 200 OK" + CRLF +
                                "Content-Type: plain/text" + CRLF +
                                "Content-Length: " + val.length() +
                                CRLF + CRLF + val;
                        System.out.println(response);
                        clientOutputStream.write(response.getBytes(StandardCharsets.US_ASCII));
                    }
                    headerPair = clientReader.readLine();
                }
            }
            default -> clientWriter.write("HTTP/1.1 404 NOT FOUND"  + CRLF + CRLF);
        }
        clientWriter.close();
    }

}
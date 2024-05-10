import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    private String dirName;

    public String getDirName() {
        return this.dirName;
    }

    public void setDirName(String directory) {
        this.dirName = directory;
    }
    static String CRLF = "\r\n";
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
        BufferedWriter clientWriter =
                new BufferedWriter(new OutputStreamWriter(clientOutputStream));
        String firstLine = clientReader.readLine();
        String cmd = firstLine.split(" ")[0];
        String path = firstLine.split(" ")[1];
        if (cmd.equals("GET")) {
            if (path.equals("/")) {
                clientOutputStream.write(("HTTP/1.1 200 OK" + CRLF + CRLF).getBytes());
            } else if (path.startsWith("/files")) {
                String filepath = path.replaceFirst("/files/", "");
                System.out.println("The filepath is: " + filepath);
                File fileAtPath = new File(dirName, filepath);
                if (!fileAtPath.exists()) {
                    String response = "HTTP/1.1 404 Not Found" + CRLF + CRLF;
                    clientOutputStream.write(response.getBytes());
                } else {
                    System.out.println("File found at filepath " + filepath);
                    BufferedReader br = new BufferedReader(new FileReader(fileAtPath));
                    String line;
                    StringBuilder fileContent = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        fileContent.append(line);
                    }
                    String response = "HTTP/1.1 200 OK" + CRLF +
                            "Content-Type: application/octet-stream" + CRLF +
                            "Content-Length: " + fileContent.length() + CRLF + CRLF +
                            fileContent;
                    System.out.println("Contents of file: " + fileContent);
                    clientOutputStream.write(response.getBytes());
                }
            } else if (path.split("/")[1].equals("echo")) {
                String echoMe = path.split("/")[2];
                String response = "HTTP/1.1 200 OK" + CRLF + "Content-Type: text/plain" +
                        CRLF + "Content-Length: " + echoMe.length() + CRLF +
                        CRLF + echoMe;
                clientWriter.write(response);
            } else if (path.equals("/user-agent")) {
                String headerPair;
                while ((headerPair = clientReader.readLine()) != null) {
                    String[] splitString = headerPair.split(":");
                    String headerKey = splitString[0];
                    String headerValue = splitString[1].strip();
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
            } else {
                clientWriter.write("HTTP/1.1 404 Not Found" + CRLF + CRLF);
            }
            clientWriter.close();
        } else if (cmd.equals("POST")) {
            String filepath = path.replaceFirst("/files/", "");
            File file = new File(dirName, filepath);
            int contentLength = 0;
            while (true) {
                String out = clientReader.readLine();
                if (out.startsWith("Content-Length: "))
                    contentLength = Integer.parseInt(out.replace("Content-Length: ", ""));
                else if (out.isEmpty()) {
                    System.out.println("BREAK");
                    break;
                }
            }
            // read file body and write it to my own file
            char[] buf = new char[contentLength];
            clientReader.read(buf);
            System.out.println(buf);
            FileWriter eee = new FileWriter(file);
            eee.write(buf);
            eee.close();

            String response = "HTTP/1.1 201 Created" + CRLF + CRLF;
            clientOutputStream.write(response.getBytes());
        } else {
            String response = "HTTP/1.1 404 Not Found" + CRLF + CRLF;
            clientOutputStream.write(response.getBytes());
        }
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
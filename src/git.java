import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class git {
    public static void main(String[] args) throws FileNotFoundException {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        //System.out.println("Logs from your program will appear here!");

//     Uncomment this block to pass the first stage

        final String command = args[0];

        switch (command) {
            case "init": {
                final File root = new File(".git");
                new File(root, "objects").mkdirs();
                new File(root, "refs").mkdirs();
                final File head = new File(root, "HEAD");

                try {
                    head.createNewFile();
                    Files.write(head.toPath(), "ref: refs/heads/main\n".getBytes());
                    System.out.println("Initialized git directory");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "cat-file": {
                String objHash = args[2];
                Path path = Paths.get(".git", "objects",
                        objHash.substring(0, 2), objHash.substring(2));

                try {
                    byte[] compressed = Files.readAllBytes(path);
                    Inflater inf = new Inflater();
                    inf.setInput(compressed);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (!inf.finished()) {
                        int decompSize = inf.inflate(buffer);
                        baos.write(buffer, 0, decompSize);
                    }
                    String decompBlob = baos.toString(StandardCharsets.UTF_8);
                    System.out.print(decompBlob.substring(
                            decompBlob.indexOf("\0") + 1));
                }
                catch (IOException | DataFormatException e) {
                    e.printStackTrace();
                }
                break;
            }
            default: System.out.println("Unknown command: " + command);
        }
    }
}
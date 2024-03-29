import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private final String serverAddress;
    private final int serverPort;
    private final String clientId;

    public ClientHandler(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientId = java.util.UUID.randomUUID().toString(); // Generate unique ID for client
    }

    public void start() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Start a thread to handle incoming messages from the server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Message from server: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Send client ID to the server
            out.println(clientId);

            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input); // Send user input to the server
            }

            // Close resources
            socket.close();
            out.close();
            in.close();
            userInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change to your server's address
        int serverPort = 12345; // Change to your server's port number
        ClientHandler clientHandler = new ClientHandler(serverAddress, serverPort);
        clientHandler.start();
    }
}

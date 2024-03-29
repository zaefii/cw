import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server {
    private final int port;
    private final Map<String, PrintWriter> clientWriters;
    private String coordinatorId;

    public Server(int port) {
        this.port = port;
        this.clientWriters = new HashMap<>();
        this.coordinatorId = null;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String clientAddress = clientSocket.getInetAddress().toString();
            String clientId = UUID.randomUUID().toString(); // Generate unique ID for client

            clientWriters.put(clientId, out);

            // Inform new client about the current coordinator
            if (coordinatorId != null) {
                out.println("Current coordinator: " + coordinatorId);
            } else {
                out.println("You are the first member of the group and the coordinator.");
                coordinatorId = clientId;
            }

            // Inform existing clients about the new client
            broadcastMessage("New member joined: " + clientId);

            String message;
            while ((message = in.readLine()) != null) {
                // Handle incoming messages from clients
                // Implement message processing and broadcasting here
            }

            // Client disconnected, remove its PrintWriter from the map
            clientWriters.remove(clientId);
            System.out.println("Client disconnected: " + clientId);

            // If coordinator leaves, select new coordinator
            if (clientId.equals(coordinatorId)) {
                selectNewCoordinator();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void broadcastMessage(String message) {
        for (PrintWriter writer : clientWriters.values()) {
            writer.println(message);
        }
    }

    private void selectNewCoordinator() {
        if (!clientWriters.isEmpty()) {
            coordinatorId = clientWriters.keySet().iterator().next(); // Select first client as new coordinator
            broadcastMessage("New coordinator selected: " + coordinatorId);
        } else {
            coordinatorId = null;
        }
    }

    public static void main(String[] args) {
        int port = 12345;
        Server server = new Server(port);
        server.start();
    }
}

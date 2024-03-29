import java.io.*; // Importing classes to manage operations for input and output
import java.net.ServerSocket; // For server-side socket operations, import the ServerSocket class.
import java.net.Socket; // Socket class import for client-side socket operations
import java.util.HashMap; // Importing the HashMap class in order to store client data
import java.util.Map; // Using the Map Interface to import and store key-value pairs
import java.util.UUID; // Importing the UUID class to provide distinct IDs

public class Server { // Declares the Server class.

    private final int port; // Declares the port number as a constant variable.

    private final Map<String, PrintWriter> clientWriters; // Map to hold client IDs and the PrintWriter objects that go with them
    private String coordinatorId; // String containing the current coordinator's ID

    public Server(int port) { // Constructor to set the port number at which the Server object is initialised
        this.port = port; // Assigns to the instance variable the port number that was supplied to the constructor
        this.clientWriters = new HashMap<>(); // Brings the clientWriters map up to date
        this.coordinatorId = null; // The coordinatorId is initialised to null
    }

    public void start() { // How to launch the server
        try { // Start of the try block where possible exceptions are handled
            ServerSocket serverSocket = new ServerSocket(port); // Establishes a new ServerSocket connected to the given port
            System.out.println("Server started on port " + port); // A message signalling server initialisation is printed
            while (true) { // Endless loop to allow client connections indefinitely
                Socket clientSocket = serverSocket.accept(); // Returns a Socket object after accepting a client connection
                System.out.println("New client connected: " + clientSocket); // Generates a message that says there is a new client connection
                new Thread(() -> handleClient(clientSocket)).start(); // Starts a new thread to handle the client
            }
        } catch (IOException e) { // Generates a message that says there is a new client connection.
            e.printStackTrace(); // The exception stack trace is printed.
        }
    }

    private void handleClient(Socket clientSocket) { // Technique for managing individual client relationships
        try { // Start of the try block where possible exceptions are handled
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // Forms a PrintWriter object in order to transmit data to the client.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Builds a BufferedReader object to access client data.

            String clientAddress = clientSocket.getInetAddress().toString(); // Obtains the IP address of the client
            String clientId = UUID.randomUUID().toString(); // Creates a distinctive ID for the customer

            clientWriters.put(clientId, out); // Connects the client's PrintWriter to its ID

            if (coordinatorId != null) { // Determines if a coordinator is already in place.
                out.println("Current coordinator: " + coordinatorId); // Notifies the incoming client who the coordinator is at this time.
            } else {
                out.println("You are the first member of the group and the coordinator."); // Notifies the new client that they are the first coordinator and member in the message
                coordinatorId = clientId; // Designates the new customer as the coordinator
            }

            broadcastMessage("New member joined: " + clientId); // Notifies current clients of the new customer

            String message;
            while ((message = in.readLine()) != null) { // Reads client messages until null is received.
                // Respond to incoming client messages
                // This is where you implement broadcasting and message processing.
            }

            clientWriters.remove(clientId); // When the client disconnects, removes their PrintWriter from the map.
            System.out.println("Client disconnected: " + clientId); // A message notifying the client's disconnection is printed

            if (clientId.equals(coordinatorId)) { // Determines whether the coordinator was the disconnected client.
                selectNewCoordinator(); // Chooses a new coordinator in the event that the coordinator was the disconnected client.
            }
        } catch (IOException e) { // If an issue arises during input/output operations, it captures the IOException.
            e.printStackTrace(); // The exception stack trace is printed.
        }
    }

    private synchronized void broadcastMessage(String message) { // A way to send a message to every client that is linked
        for (PrintWriter writer : clientWriters.values()) { // Goes over each PrintWriter object in the clientWriters map one by one.
            writer.println(message); // Delivers the word to every customer
        }
    }

    private void selectNewCoordinator() { // Technique for choosing a new coordinator from the linked clientele
        if (!clientWriters.isEmpty()) { // Determines whether any clients are connected
            coordinatorId = clientWriters.keySet().iterator().next(); // Chooses the initial client to serve as the new coordinator
            broadcastMessage("New coordinator selected: " + coordinatorId); // Designates the initial client as the new coordinator
        } else {
            coordinatorId = null; // If there are no connected clients, sets coordinatorId to null.
        }
    }

    public static void main(String[] args) { // Primary means of initiating the server
        int port = 12345; // Gives the port number that the server will use to accept connections.
        Server server = new Server(port); // Establishes a new Server object using the given port.
        server.start(); // Initiates the server
    }
}

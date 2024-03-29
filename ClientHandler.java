import java.io.*; // Importing classes to manage operations for input and output
import java.net.Socket; // For client-side socket operations, import the Socket class.

public class ClientHandler { // Declares the ClientHandler class.
    private final String serverAddress; // Creates a final variable to hold the address of the server.
    private final int serverPort; // Creates a final variable to hold the port of the server.
    private final String clientId; // Establishes a last variable to hold the client ID.

    public ClientHandler(String serverAddress, int serverPort) { // Initialise the ClientHandler object with the server address and port using the constructor.
        this.serverAddress = serverAddress; // Assigns the instance variable to the server address supplied to the constructor.
        this.serverPort = serverPort; // Assigns to the instance variable the server port that was supplied to the constructor.
        this.clientId = java.util.UUID.randomUUID().toString(); // Creates a distinctive ID for the customer
    }

    public void start() { // Method to start the client
        try { // Start of the try block where possible exceptions are handled
            Socket socket = new Socket(serverAddress, serverPort); // Establishes a new socket with the server address and port attached.
            System.out.println("Connected to server: " + serverAddress + ":" + serverPort); // Prints a message confirming that the connection to the server was successful
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // In order to communicate data to the server, create a PrintWriter object.
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // To read data from the server, creates an object called BufferedReader.
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); // Constructs a BufferedReader object to receive console input from users.

            // Create a thread to deal with the server's incoming messages.
            new Thread(() -> { // Starts a new thread
                try { // Try block's initialisation for managing possible exceptions
                    String message; // Creates a variable to hold receiving server messages
                    while ((message = in.readLine()) != null) { // Reads messages from the server in order to obtain null
                        System.out.println("Message from server: " + message); // Prints the message that was received from the server.
                    }
                } catch (IOException e) { // If an issue arises during input/output operations, it captures the IOException.
                    e.printStackTrace(); // The exception stack trace is printed.
                }
            }).start(); // Starts the thread

            out.println(clientId); // Sends the server the client ID.

            String input; // Establishes a variable to hold user data
            while ((input = userInput.readLine()) != null) { // Scans the console for user input until null is detected.
                out.println(input); // Transmits the user's input to the host
            }

            // Close resources
            socket.close(); // Closes the socket
            out.close(); // Closes the PrintWriter
            in.close(); // Closes the BufferedReader for input from the server
            userInput.close(); // Closes the BufferedReader to prevent console input from users.
        } catch (IOException e) { // Catches IOException in the event that a problem arises when using a socket.
            e.printStackTrace(); // Prints the exception stack trace
        }
    }

    public static void main(String[] args) { // Main method to start the client
        String serverAddress = "localhost"; // Gives the server address (which, if necessary, can be changed to the real server address).
        int serverPort = 12345; // Gives the port number of the server (which can be changed to the real port number if necessary).
        ClientHandler clientHandler = new ClientHandler(serverAddress, serverPort); // Establishes a new ClientHandler object using the given port and server address.
        clientHandler.start(); // Starts the client
    }
}

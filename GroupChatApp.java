import java.util.HashMap; // Importing the HashMap class to store the members of the group
import java.util.Map; // Using the Map Interface to import and store key-value pairs
import java.util.Scanner; // Scanner class import for user input

public class GroupChatApp { // Declares the GroupChatApp class.
    private final Map<String, GroupMember> members; // A map to keep track of group members
    private String coordinatorId; // String to hold the coordinator's ID
    private boolean isCoordinator; // Boolean to show if the coordinator is the current member
    private final Scanner scanner; // Object scanner for user input

    public GroupChatApp() { // Initialise the GroupChatApp object using the constructor.
        this.members = new HashMap<>(); // Sets the members map's initial state.
        this.coordinatorId = null; // Sets the coordinatorId to null at first
        this.isCoordinator = false; // Sets isCoordinator to false during startup.
        this.scanner = new Scanner(System.in); // Sets up the scanner for input from the user.
    }

    public void start() { // How to launch the group chat software
        System.out.println("Welcome to Group Chat"); // Welcome message is printed

        String id = getUserInput("Enter your ID: "); // Requests that the user input their ID.
        String ipAddress = getUserInput("Enter server IP: "); // Requests that the user input the server IP
        int port = Integer.parseInt(getUserInput("Enter server port: ")); // Requests that the user input the server port.

        addMember(id, ipAddress, port); // Incorporates the current user into the group

        while (true) { // Infinite loop that responds to user input
            System.out.println("\nEnter command (send <message>, members, coordinator, quit): "); // Requests that the user input a command
            String command = scanner.nextLine(); // Interprets the user's order

            if (command.equalsIgnoreCase("quit")) { // Determines whether the user wishes to close the programme
                leaveGroup(id); // Takes the person out of the group
                break; // Closes the loop
            } else if (command.startsWith("send")) { // Determines whether the user wants to send a message
                String message = command.substring(5).trim(); // The message is taken out of the command
                sendMessage(id, message); // Communicates with the group
            } else if (command.equalsIgnoreCase("members")) { // Determines if the user wishes to see the members of the group.
                printGroupDetails(); // Prints each group member's details
            } else if (command.equalsIgnoreCase("coordinator")) { // Determines whether the user desires to see the coordinator
                if (coordinatorId != null) { // Verifies if a coordinator has been chosen
                    System.out.println("Current coordinator: " + coordinatorId); // Prints the current coordinator's ID.
                } else {
                    System.out.println("No coordinator selected."); // Displays a notice saying that no coordinator has been chosen.
                }
            } else {
                System.out.println("Invalid command. Please try again."); // Prints a message if a command is not valid.
            }
        }

        scanner.close(); // Shuts down the scanner
    }

    private String getUserInput(String prompt) { // How to obtain input from the user
        System.out.print(prompt); // Prints the instruction
        return scanner.nextLine(); // Gives the user's input back
    }

    private void addMember(String id, String ipAddress, int port) { // Procedure for adding someone to the group
        GroupMember member = new GroupMember(id, ipAddress, port); // Establishes a new GroupMember object.
        members.put(id, member); // The member is added to the map of members.
        if (coordinatorId == null) { // Verifies if the coordinator is absent.
            coordinatorId = id; // Makes the current participant the coordinator
            isCoordinator = true; // Makes the person taking part now the coordinator
            System.out.println("You are the coordinator."); // Prints a message stating that the coordinator is the current member.
        } else {
            System.out.println("You have joined the group."); // Prints a notification stating that the new member has joined the group.
            System.out.println("Current coordinator is: " + coordinatorId); // Prints the current coordinator's ID.
        }
    }

    private void sendMessage(String senderId, String message) { // How to communicate with the group
        System.out.println("Message from " + senderId + ": " + message); // The message prints
    }

    private void leaveGroup(String id) { // How to take someone out of the group
        members.remove(id); // Takes the person off of the members map.

        if (id.equals(coordinatorId)) { // Verifies that the individual is the coordinator
            selectNewCoordinator(); // Chooses a new coordinator
        }

        System.out.println("Member " + id + " has left the group."); // Prints a notification that the group member has departed.
    }

    private void selectNewCoordinator() { // How a new coordinator is chosen
        if (!members.isEmpty()) { // Determines whether the group has any members.
            coordinatorId = members.keySet().iterator().next(); // Chooses the initial participant to be the next coordinator.
            System.out.println("New coordinator selected: " + coordinatorId); // Prints a notification naming the coordinator
        } else {
            coordinatorId = null; // If there are no members in the group, sets the coordinator ID to null.
        }
    }

    private void printGroupDetails() { // How to print every group member's details
        System.out.println("Group Members:"); // Prints a group member's header
        for (GroupMember member : members.values()) { // Repeats for every member of the group.
            System.out.println("ID: " + member.id() + ", IP: " + member.ipAddress() + ", Port: " + member.port()); // Prints each member's details
        }
        System.out.println("Coordinator: " + coordinatorId); // Prints the coordinator's ID
    }

    public static void main(String[] args) { // Primary means of launching the programme
        GroupChatApp groupChatApp = new GroupChatApp(); // Establishes a new GroupChatApp object
        groupChatApp.start(); // Starts the group chat application
    }

    private record GroupMember(String id, String ipAddress, int port) { // Group member's representing record
    }
}

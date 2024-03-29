import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GroupChatApp {
    private final Map<String, GroupMember> members;
    private String coordinatorId;
    private boolean isCoordinator;
    private final Scanner scanner;

    public GroupChatApp() {
        this.members = new HashMap<>();
        this.coordinatorId = null;
        this.isCoordinator = false;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Group Chat");

        String id = getUserInput("Enter your ID: ");
        String ipAddress = getUserInput("Enter server IP: ");
        int port = Integer.parseInt(getUserInput("Enter server port: "));

        addMember(id, ipAddress, port);

        while (true) {
            System.out.println("\nEnter command (send <message>, members, coordinator, quit): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("quit")) {
                leaveGroup(id);
                break;
            } else if (command.startsWith("send")) {
                String message = command.substring(5).trim();
                sendMessage(id, message);
            } else if (command.equalsIgnoreCase("members")) {
                printGroupDetails();
            } else if (command.equalsIgnoreCase("coordinator")) {
                if (coordinatorId != null) {
                    System.out.println("Current coordinator: " + coordinatorId);
                } else {
                    System.out.println("No coordinator selected.");
                }
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }

        scanner.close();
    }

    private String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void addMember(String id, String ipAddress, int port) {
        GroupMember member = new GroupMember(id, ipAddress, port);
        members.put(id, member);
        if (coordinatorId == null) {
            coordinatorId = id;
            isCoordinator = true;
            System.out.println("You are the coordinator.");
        } else {
            System.out.println("Current coordinator: " + coordinatorId);
            System.out.println("You have joined the group.");
            System.out.println("Current coordinator is: " + coordinatorId);
        }
    }

    private void sendMessage(String senderId, String message) {
        System.out.println("Message from " + senderId + ": " + message);
    }

    private void leaveGroup(String id) {
        members.remove(id);
        if (id.equals(coordinatorId)) {
            selectNewCoordinator();
        }
        System.out.println("Member " + id + " has left the group.");
    }

    private void selectNewCoordinator() {
        if (!members.isEmpty()) {
            coordinatorId = members.keySet().iterator().next();
            System.out.println("New coordinator selected: " + coordinatorId);
        } else {
            coordinatorId = null;
        }
    }

    private void printGroupDetails() {
        System.out.println("Group Members:");
        for (GroupMember member : members.values()) {
            System.out.println("ID: " + member.getId() + ", IP: " + member.getIpAddress() + ", Port: " + member.getPort());
        }
        System.out.println("Coordinator: " + coordinatorId);
    }

    public static void main(String[] args) {
        GroupChatApp groupChatApp = new GroupChatApp();
        groupChatApp.start();
    }

    private static class GroupMember {
        private final String id;
        private final String ipAddress;
        private final int port;

        public GroupMember(String id, String ipAddress, int port) {
            this.id = id;
            this.ipAddress = ipAddress;
            this.port = port;
        }

        public String getId() {
            return id;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public int getPort() {
            return port;
        }
    }
}

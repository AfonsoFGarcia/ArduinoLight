import java.io.*;
import java.net.*;

public class GuessLight {
    private static String TCP_IP;
    private static Integer TCP_PORT = 5000;

    private static String sendToServer(String message) throws IOException {
        Socket server = new Socket(TCP_IP, TCP_PORT);
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

        out.println(message);
        String reply =  in.readLine();
        server.close();

        return reply;
    }

    private static void printMenu() {
        System.out.println("0 - Exit the game");
        System.out.println("1 - Guess the number");
        System.out.println("2 - Reset the number");
        System.out.print("Choose option: ");
    }

    private static void guess() throws IOException {
        System.out.print("Enter your guess [X X X]: ");
        String guess = System.console().readLine();
        sendToServer("G "+guess);
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("USAGE: java GuessLight [SERVER_IP]");
            System.exit(-1);
        }
        TCP_IP = args[0];
        while(true) {
            printMenu();
            String option = System.console().readLine();
            try {
                if (option.equals("0")) {
                    break;
                } else if (option.equals("1")) {
                    guess();
                } else if (option.equals("2")) {
                    sendToServer("RES");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
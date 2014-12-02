import java.io.*;
import java.net.*;

public class GuessLight {
    private static String TCP_IP;
    private static Integer TCP_PORT = 5000;

    private static void printMenu() {
        System.out.println("\n1 - Guess the number");
        System.out.println("2 - Reset the number\n");
        System.out.println("0 - Exit the game\n");
        System.out.print("Choose option: ");
    }

    private static void guess() {
        System.out.print("\nEnter your guess [X X X]: ");
        String guess = System.console().readLine();
        new Thread(new SendMessage("G "+guess)).start();
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
            if (option.equals("0")) {
                break;
            } else if (option.equals("1")) {
                guess();
            } else if (option.equals("2")) {
                new Thread(new SendMessage("RES")).start();
            }
        }
    }

    private static class SendMessage implements Runnable {
        private String message;

        public SendMessage(String message) {
            this.message = message;
        }

        public void run() {
            try {
                Socket server = new Socket(TCP_IP, TCP_PORT);
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

                out.println(message);
                String reply =  in.readLine();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
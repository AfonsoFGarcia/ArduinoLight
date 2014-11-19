import java.io.*;
import java.net.*;

class Client {
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Usaga: java Client [SERVER IP] [COMMAND]");
			return;
		}
		
		String TCP_IP = args[0];
		int TCP_PORT = 5000;

		try {
			Socket server = new Socket(TCP_IP, TCP_PORT);
			PrintWriter out = new PrintWriter(server.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			out.println(args[1]);
			System.out.println("Received data: " + in.readLine());
		} catch (UnknownHostException e) {
            System.err.println("Don't know about host " + TCP_IP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                TCP_IP);
            System.exit(1);
        }
	}
}